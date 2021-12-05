package selenium.pageobjects;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WebLabAnswerPage extends BasePageObject {

    public WebLabAnswerPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/div/div[3]/div[1]/div/div/div[1]")
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[2]/div[5]/div/div/div[3]/div[1]/div/div/a[1]")
    private WebElement unlockAnswerButton;

    public String getSolution() {
        this.awaitPageLoaded();

        // unlock answer if it is not yet unlocked
        if (this.isDisplayed(unlockAnswerButton)) {
            this.unlockAnswerButton.click();
            this.awaitElementVisibility(this.solutionDiv);
        }

        String script = "return document.getElementById('solution').value;";

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String solution = (String) js.executeScript(script);

        return solution;
    }

    private void awaitPageLoaded() {
        waitUntil(30, () -> this.isDisplayed(solutionDiv) || this.isDisplayed(unlockAnswerButton));
    }
}
