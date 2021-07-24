package nl.tudelft.cse1110.grader.integration;

import org.assertj.core.api.Condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraderIntegrationTestAssertions {

    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
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
                return matcher.find();
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

}
