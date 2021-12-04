package selenium;

import org.junit.jupiter.params.provider.Arguments;
import selenium.pageobjects.WebLabAssignmentListPage;

import java.util.stream.Stream;

public class AllExerciseTests extends WebLabSeleniumTestBase {

    protected static final String BASE_ASSIGNMENT_ID = "91800";

    public Stream<Arguments> scrapeAssignmentUrls() {
        return scrapeAssignmentUrls(WEBLAB_URL + String.format(WEBLAB_ASSIGNMENT_INFO_PATH, BASE_ASSIGNMENT_ID));
    }

    private Stream<Arguments> scrapeAssignmentUrls(String baseAssignmentUrl) {
        WebLabAssignmentListPage listPage = new WebLabAssignmentListPage(driver, baseAssignmentUrl);
        listPage.navigate();

        // Scrape programming assignments
        Stream<Arguments> assignmentUrls = listPage.getAssignmentUrls().stream().map(Arguments::of);

        // Recursively scrape assignments in subdirectories
        for (String subdirectoryUrl : listPage.getSubdirectoryUrls()) {
            Stream<Arguments> subdirectoryUrls = scrapeAssignmentUrls(subdirectoryUrl);
            assignmentUrls = Stream.concat(assignmentUrls, subdirectoryUrls);
        }

        return assignmentUrls;
    }
}
