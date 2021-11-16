package selenium;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import selenium.pageobjects.WebLabLoginPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.fail;

public abstract class WebLabSeleniumTestBase {
    // Remember to set the credentials as an environment variable ("username:password")
    // Only works with local WebLab accounts, NOT netid
    private static final String WEBLAB_CREDENTIALS_ENV_VAR = "WEBLAB_CREDENTIALS";
    protected static final String WEBLAB_URL = "https://weblab.tudelft.nl";
    private static final String WEBLAB_LOGIN_PATH = "/signin";

    protected static final String USER_ID = "36461";

    protected static final String WEBLAB_SUBMISSION_PATH = "///assignment/%s/submission/%s/edit";

    protected WebDriver driver;
    private String weblabUsername;
    private String weblabPassword;

    protected String testSubmissionContent;

    @BeforeEach
    public void setup() throws IOException {
        String credentialsString = System.getenv(WEBLAB_CREDENTIALS_ENV_VAR);
        String[] weblabCredentials = credentialsString != null ? credentialsString.split(":") : null;

        if (weblabCredentials == null || weblabCredentials.length != 2) {
            fail("WebLab credentials are not set configured. Provide a local WebLab username and " +
                 "password in the \"" + WEBLAB_CREDENTIALS_ENV_VAR + "\" environment variable in the format " +
                 "\"username:password\" or \"email:password\". The provided user has to be enrolled in the " +
                 "course containing the test assignments.");
            return;
        }

        this.weblabUsername = weblabCredentials[0];
        this.weblabPassword = weblabCredentials[1];

        this.driver = new FirefoxDriver();

        this.testSubmissionContent = Files.readString(Path.of(
                ResourceUtils.resourceFolder("/selenium/solutions/") + "Upvote.java"));

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
}
