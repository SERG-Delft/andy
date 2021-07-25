package nl.tudelft.cse1110.grader.integration;

import org.assertj.core.api.Condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * I created a assertj helper for the assertion. Such helpers are fundamental in this type of code.
 * We return strings, and these strings might change. We don't want to have to change in multiple times if that happens.
 * That's why the assertions happen in the helper methods.
 * If we change the output, all we need to do is to change the assertion helper.
 * And the assertion looks nicer now, e.g., assertThat(result).has(numberOfJUnitTestsPassing(31));
 *
 */
public class GraderIntegrationTestAssertions {


    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed";  // d+ means anything can match
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
                String regex = "--- JUnit execution\\n\\d+\\/" + numberOfTests + " passed"; // \d matches any digit
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.lookingAt();
            }
        };
    }


    public static Condition<String> compilationFailure() {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- Compilation\\nFailure\\n\\nSee the compilation errors below:";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> compilationErrorOnLine(int lineNumber) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- line " + lineNumber + ":\\s";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> compilationErrorType(String errorType) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- line \\d+:\\n  " + errorType;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }



    public static Condition<String> failingTestName(String testName) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- Test " + "\"" + testName + "\\(" + "\\)" + "\"" + " failed:";     // () and " " are escaped
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }


    public static Condition<String> errorType(String errorType) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "\\w." + errorType;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }



    public static Condition<String> failingParameterizedTestName(String testName) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- Parameterized test " + "\"" + testName + "\"" + ",";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }


    public static Condition<String> parameterizedTestCaseNumber(int testCaseNumber) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = " test case #" + testCaseNumber + " failed:";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }






}
