package delft;

import org.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

class CSE1110 {
    public static WebDriver createDriver() {
        String dir = System.getProperty("assignment.directory")==null ?
                System.getProperty("user.dir") :
                System.getProperty("assignment.directory");

        String url = "file://" + dir + "/TodaysEvents/index.html";

        WebDriver driver = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);
        driver.get(url);
        return driver;
    }
}
