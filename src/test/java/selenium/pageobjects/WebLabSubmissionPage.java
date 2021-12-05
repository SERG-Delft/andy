package selenium.pageobjects;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

public class WebLabSubmissionPage extends BasePageObject {

    public WebLabSubmissionPage(WebDriver driver, String url) {
        super(driver, url);
    }

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[1]/div")
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/span")
    private WebElement saveButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[2]")
    private WebElement runOnlyTestsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[3]")
    private WebElement runWithCoverageBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[4]")
    private WebElement assessWithoutHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/span/div/a[5]")
    private WebElement assessWithHintsBtn;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div/div/div/pre")
    private WebElement output;

    public void runOnlyTests() {
        this.runOnlyTestsBtn.click();
        this.awaitExecutionStart();
    }

    public void runWithCoverage() {
        this.runWithCoverageBtn.click();
        this.awaitExecutionStart();
    }

    public void assessWithoutHints() {
        this.assessWithoutHintsBtn.click();
        this.awaitExecutionStart();
    }

    public void assessWithHints() {
        this.assessWithHintsBtn.click();
        this.awaitExecutionStart();
    }

    public void enterSolution(String solution) {
        this.awaitElementVisibility(solutionDiv);

        this.solutionDiv.click();
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("a");
        actions.keyUp(Keys.CONTROL);
        actions.sendKeys(Keys.BACK_SPACE);
        actions.sendKeys(solution);
        actions.perform();
        this.saveButton.click();

        // Wait a few seconds for the submission to be processed
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignore
        }
        waitUntil(15, () -> saveButton.getText().equals("Saved"));
    }

    public String getOutput() {
        // Wait until WebLab finishes processing submission
        waitUntil(90, () -> output.getText().contains("Status: Done"));
        return this.output.getText();
    }

    private void awaitExecutionStart() {
        waitUntil(30, () -> !output.getText().contains("Status: Done"));
    }
}
