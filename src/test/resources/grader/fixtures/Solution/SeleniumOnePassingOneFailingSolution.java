package delft;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SeleniumOnePassingOneFailingTest {
    @Test
    void demoOfAPassingTest() {
        // select which driver to use
        WebDriver browser = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);

        // visit a page
        browser.get("http://localhost:8087");

        // find an HTML element in the page
        WebElement welcomeHeader = browser.findElement(By.tagName("p"));

        // assert it contains what we want
        assertThat(welcomeHeader.getText()).isEqualTo("Hello");

        // close the browser and the selenium session
        browser.close();
    }

    @Test
    void demoOfAFailingTest() {
        // select which driver to use
        WebDriver browser = new HtmlUnitDriver(BrowserVersion.FIREFOX, true);

        // visit a page
        browser.get("http://localhost:8087");

        // find an HTML element in the page
        WebElement welcomeHeader = browser.findElement(By.tagName("p"));

        // assert it contains what we want
        assertThat(welcomeHeader.getText()).isEqualTo("Welcome");

        // close the browser and the selenium session
        browser.close();
    }

}
