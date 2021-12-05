package selenium;

import org.junit.jupiter.params.provider.Arguments;
import selenium.pageobjects.WebLabAssignmentListPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class AllExerciseTests extends WebLabSeleniumTestBase {

    protected static final String BASE_ASSIGNMENT_ID = "91800";

    public Stream<Arguments> scrapeAssignmentIds() {
        return scrapeAssignmentIds(WEBLAB_URL + String.format(WEBLAB_ASSIGNMENT_VIEW_PATH, BASE_ASSIGNMENT_ID));
    }

    private Stream<Arguments> scrapeAssignmentIds(String baseAssignmentUrl) {
        WebLabAssignmentListPage listPage = new WebLabAssignmentListPage(driver, baseAssignmentUrl);
        listPage.navigate();

        // Scrape programming assignments
        Stream<Arguments> assignmentIds = listPage.getAssignmentUrls().stream()
                .map(AllExerciseTests::getAssignmentId)
                .map(Arguments::of);

        // Recursively scrape assignments in subdirectories
        for (String subdirectoryUrl : listPage.getSubdirectoryUrls()) {
            Stream<Arguments> subdirectoryAssignmentIds = scrapeAssignmentIds(subdirectoryUrl);
            assignmentIds = Stream.concat(assignmentIds, subdirectoryAssignmentIds);
        }

        return assignmentIds;
    }

    private static String getAssignmentId(String path) {
        Pattern pattern = Pattern.compile("/assignment/(\\d+)/");
        Matcher matcher = pattern.matcher(path);

        if (!matcher.find()) {
            throw new IllegalArgumentException("Could not parse path");
        }

        return matcher.group(1);
    }
}
