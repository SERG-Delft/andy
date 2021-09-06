package selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import selenium.pageobjects.WebLabLoginPage;

public class WebLabSeleniumTest {
    // Remember to set the credentials as an environment variable ("username:password")
    // Only works with local WebLab accounts, NOT netid
    private static final String WEBLAB_CREDENTIALS_ENV_VAR = "WEBLAB_CREDENTIALS";
    private static final String WEBLAB_URL = "https://weblab.tudelft.nl";
    private static final String WEBLAB_LOGIN_PATH = "/signin";

    private WebDriver driver;
    private String weblabUsername;
    private String weblabPassword;

    @BeforeEach
    public void setup() {
        this.driver = new FirefoxDriver();

        String[] weblabCredentials = System.getenv(WEBLAB_CREDENTIALS_ENV_VAR).split(":");
        this.weblabUsername = weblabCredentials[0];
        this.weblabPassword = weblabCredentials[1];

        this.login();
    }

    @AfterEach
    public void cleanup() {
        driver.quit();
    }

    private void login() {
        WebLabLoginPage loginPage = new WebLabLoginPage(this.driver, WEBLAB_URL + WEBLAB_LOGIN_PATH);
        loginPage.navigate();
        loginPage.login(this.weblabUsername, this.weblabPassword);
    }

    @Test
    public void testSubmission() {
    }
}
