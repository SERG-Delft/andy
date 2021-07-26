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


    public static Condition<String> errorMessage(String errorMessage) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = errorMessage;
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


    public static Condition<String> uninvokedMethod(String uninvokedMethod) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "Wanted but not invoked:\n" + uninvokedMethod;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }


    public static Condition<String> hintAtInteractionFound(String invokedMethod) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "However, there was exactly 1 interaction with this mock:\n" + invokedMethod;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }







}
