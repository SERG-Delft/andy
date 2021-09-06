package selenium.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Pattern;

public class WebLabSubmissionPage {
    private final WebDriver driver;
    private final String url;

    public WebLabSubmissionPage(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[1]/div")
    private WebElement solutionDiv;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/span")
    private WebElement saveButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div[2]/a[2]")
    private WebElement specTestButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div[2]/a[3]")
    private WebElement submitButton;

    @FindBy(xpath = "/html/body/div[3]/div[5]/div/div/div[3]/div[2]/div/div[1]/div[2]/div[2]/pre")
    private WebElement output;

    public void navigate() {
        this.driver.navigate().to(url);
    }

    public void runSpecTests() {
        this.specTestButton.click();
    }

    public void enterSolution(String solution) {
        this.solutionDiv.click();
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.CONTROL);
        actions.sendKeys("a");
        actions.keyUp(Keys.CONTROL);
        actions.sendKeys(Keys.BACK_SPACE);
        actions.sendKeys(solution);
        actions.perform();
        this.saveButton.click();
    }

    public String getOutput() {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        wait.until(ExpectedConditions.textMatches(By.id(output.getAttribute("id")),
                Pattern.compile("Status: Done")));
        return this.output.getText();
    }

    public void submitSolution() {
        this.submitButton.click();
    }
}
