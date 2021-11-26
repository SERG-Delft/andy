package selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePageObject {
    protected final WebDriver driver;
    protected final String url;

    protected BasePageObject(WebDriver driver, String url) {
        this.driver = driver;
        this.url = url;
        PageFactory.initElements(driver, this);
    }

    public void navigate() {
        this.driver.navigate().to(url);
    }

    protected void awaitElementVisibility(WebElement... elements) {
        WebDriverWait wait = new WebDriverWait(this.driver, 10);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }
}
