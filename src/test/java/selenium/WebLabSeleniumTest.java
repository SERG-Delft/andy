package selenium;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import selenium.pageobjects.WebLabLoginPage;
import selenium.pageobjects.WebLabSubmissionPage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static testutils.WebLabTestAssertions.*;

public class WebLabSeleniumTest {
    // Remember to set the credentials as an environment variable ("username:password")
    // Only works with local WebLab accounts, NOT netid
    private static final String WEBLAB_CREDENTIALS_ENV_VAR = "WEBLAB_CREDENTIALS";
    private static final String WEBLAB_URL = "https://weblab.tudelft.nl";
    private static final String WEBLAB_LOGIN_PATH = "/signin";

    private static final String ASSIGNMENT_PRACTICE = "89104";
    private static final String ASSIGNMENT_EXAM = "89106";
    private static final String USER_ID = "36461";

    private static final String WEBLAB_SUBMISSION_PATH = "///assignment/%s/submission/%s/edit";

    private WebDriver driver;
    private String weblabUsername;
    private String weblabPassword;

    private String submissionContent;

    @BeforeEach
    public void setup() throws IOException {
        String credentialsString = System.getenv(WEBLAB_CREDENTIALS_ENV_VAR);
        String[] weblabCredentials = credentialsString != null ? credentialsString.split(":") : null;

        if(weblabCredentials == null || weblabCredentials.length != 2){
            fail("WebLab credentials are not set configured. Provide a local WebLab username and " +
                 "password in the \""+ WEBLAB_CREDENTIALS_ENV_VAR + "\" environment variable in the format " +
                 "\"username:password\" or \"email:password\". The provided user has to be enrolled in the " +
                 "course containing the test assignments.");
            return;
        }

        this.weblabUsername = weblabCredentials[0];
        this.weblabPassword = weblabCredentials[1];

        this.driver = new FirefoxDriver();

        this.submissionContent = Files.readString(Path.of(
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

    @Test
    public void testPracticeSubmission() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_PRACTICE, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.submissionContent);
        webLabSubmissionPage.runSpecTests();

        String output = webLabSubmissionPage.getOutput();

        webLabSubmissionPage.submitSolution();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(linesCovered(10))
                .has(instructionsCovered(31))
                .has(branchesCovered(2))
                .has(mutationScore(3, 4))
                .has(scoreOfCodeChecks(17, 18))
                .has(codeCheck("UserRepository should be mocked", true, 1))
                .has(codeCheck("Scoring should be mocked", true, 1))
                .has(codeCheck("StackOverflow should not be mocked", true, 1))
                .has(codeCheck("Post should not be mocked", true, 1))
                .has(codeCheck("Spies should not be used", true, 1))
                .has(codeCheck("pointsForFeaturedPost should be set up", true, 2))
                .has(codeCheck("pointsForNormalPost should be set up", true, 2))
                .has(codeCheck("set up pointsForFeaturedPost just once", true, 1))
                .has(codeCheck("set up pointsForNormalPost just once", true, 1))
                .has(codeCheck("update should be verified in both tests", true, 3))
                .has(codeCheck("tests should have assertions", false, 1))
                .has(codeCheck("getPoints should have an assertion", true, 3))
                .has(metaTestsPassing(2))
                .has(metaTests(3))
                .has(metaTestPassing("does not update"))
                .has(metaTestFailing("does not add points if post is not featured"))
                .has(metaTestPassing("change condition"))
                .has(fullGradeDescription("Branch coverage", 2, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 3, 4, 0.25))
                .has(fullGradeDescription("Code checks", 17, 18, 0.25))
                .has(fullGradeDescription("Meta tests", 2, 3, 0.25))
                .contains("Final grade: 84/100")
                .has(mode("PRACTICE"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 84/100");
    }

    @Test
    public void testExamSubmission() {
        WebLabSubmissionPage webLabSubmissionPage = new WebLabSubmissionPage(driver,
                WEBLAB_URL + String.format(WEBLAB_SUBMISSION_PATH, ASSIGNMENT_EXAM, USER_ID));

        webLabSubmissionPage.navigate();
        webLabSubmissionPage.enterSolution(this.submissionContent);
        webLabSubmissionPage.runSpecTests();

        String output = webLabSubmissionPage.getOutput();

        webLabSubmissionPage.submitSolution();

        assertThat(output)
                .has(compilationSuccess())
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(2))
                .has(linesCovered(10))
                .has(instructionsCovered(31))
                .has(branchesCovered(2))
                .has(mutationScore(3, 4))
                .has(noCodeChecks())
                .has(not(scoreOfCodeChecks(17, 18)))
                .has(not(codeCheck("tests should have assertions", false, 1)))
                .has(noMetaTests())
                .has(not(metaTestsPassing(2)))
                .has(not(metaTests(3)))
                .has(not(metaTestPassing("does not update")))
                .has(not(metaTestFailing("does not add points if post is not featured")))
                .has(noFinalGrade())
                .has(not(fullGradeDescription("Branch coverage", 2, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Code checks", 17, 18, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .doesNotContain("Final grade:")
                .has(mode("EXAM"))
                .contains("pitest")
                .contains("jacoco")
                .contains("Test score: 0/100");
    }
}
