package delft;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

class CSE1110 {
    public static WebDriver getDriver() {
        String url = "file://" + System.getProperty("user.dir") + "/BookStore/index.html";

        WebDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);
        driver.get(url);
        return driver;
    }
}
