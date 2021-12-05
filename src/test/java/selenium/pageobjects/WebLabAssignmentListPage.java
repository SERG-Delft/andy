package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

public class WebLabAssignmentListPage extends BasePageObject {

    public WebLabAssignmentListPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(className = "majorrow")
    private List<WebElement> subAssignments;

    public List<String> getSubdirectoryUrls() {
        return getSubAssignmentUrls("Collection of Assignments");
    }

    public List<String> getAssignmentUrls() {
        return getSubAssignmentUrls("Programming question (docker:cse1110-andy)");
    }

    private List<String> getSubAssignmentUrls(String type) {
        this.awaitListLoaded();

        return subAssignments.stream()
                .filter(tr -> tr
                        .findElements(By.tagName("td"))
                        .get(0)
                        .findElement(By.tagName("span"))
                        .getAttribute("data-original-title")
                        .equals(type)
                )
                .map(tr -> tr
                        .findElements(By.tagName("td"))
                        .get(1)
                        .findElement(By.tagName("a"))
                        .getAttribute("href")
                )
                .collect(Collectors.toList());
    }

    private void awaitListLoaded() {
        waitUntil(30, () -> !subAssignments.isEmpty());
    }
}
