package selenium.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

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
}
