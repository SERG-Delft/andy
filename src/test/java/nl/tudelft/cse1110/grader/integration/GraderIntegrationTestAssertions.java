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


    public static Condition<String> propertyTestFailing(String testName) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- Property test \"" + testName + "\" failed:\n\\{.*" + testName;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);

                return matcher.find();
            }
        };
    }


    public static Condition<String> parameterizedTestFailing(String testName, int testCaseNumber) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- Parameterized test \"" + testName + "\", test case #" + testCaseNumber + " failed:";
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


    public static Condition<String> compilationSuccess() {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "--- Compilation\\nSuccess";
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


    public static Condition<String> compilationErrorMoreTimes(String errorType, int times) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- line \\d+:\\n  " + errorType;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                int count = 0;
                while (matcher.find()) {
                    count++;
                }

                return count == times;
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

    public static Condition<String> linesCovered(int numberOfLinesCovered) {
        return new Condition<>() {

            @Override
            public boolean matches(String value) {
                String regex = "Line coverage: " + numberOfLinesCovered + "/\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> instructionsCovered(int numberOfInstructionsCovered) {
        return new Condition<>() {

            @Override
            public boolean matches(String value) {
                String regex = "Instruction coverage: " + numberOfInstructionsCovered + "/\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> branchesCovered(int numberOfBranchesCovered) {
        return new Condition<>() {

            @Override
            public boolean matches(String value) {
                String regex = "Branch coverage: " + numberOfBranchesCovered + "/\\d+";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

}
