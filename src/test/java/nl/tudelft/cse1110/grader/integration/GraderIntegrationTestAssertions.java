package nl.tudelft.cse1110.grader.integration;

import org.assertj.core.api.Condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.getSeparateTests;

public class GraderIntegrationTestAssertions {

    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.lookingAt();
            }
        };
    }

    public static Condition<String> totalNumberOfJUnitTests(int numberOfTests) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- JUnit execution\\n\\d+\\/" + numberOfTests + " passed";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.lookingAt();
            }
        };
    }

    public static Condition<String> propertyTestFailing(String testName) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String[] testCases = getSeparateTests(value);

                for (String testCase : testCases) {
                    String regex = "Property test \"" + testName + "\" failed:.*(-)+jqwik(-)+.*";
                    Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(testCase);

                    if (matcher.find()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Condition<String> parameterizedTestFailing(String testName, int testCaseNumber) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- Parameterized test \"" + testName + "\", test case #" + testCaseNumber + " failed:";
                Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }
}
