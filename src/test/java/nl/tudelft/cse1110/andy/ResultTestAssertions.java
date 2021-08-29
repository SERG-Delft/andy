package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.grader.util.FilesUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Condition;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.not;


public class ResultTestAssertions {

    private static Condition<String> containsRegex(String regex) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    private static Condition<String> containsString(String regex) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains(regex);
            }
        };
    }

    public static Condition<String> compilationErrorMoreTimes(String errorType, int times) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                String regex = "- line \\d+: " + errorType;
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

    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return containsRegex("--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed");
    }


    public static Condition<String> totalNumberOfJUnitTests(int numberOfTests) {
        return containsRegex("--- JUnit execution\\n\\d+\\/" + numberOfTests + " passed");
    }


    public static Condition<String> propertyTestFailing(String testName) {
        return containsRegex("- Property test \"" + testName + "\" failed:\n\\{.*" + testName);
    }


    public static Condition<String> parameterizedTestFailing(String testName, int testCaseNumber) {
        return containsString("- Parameterized test \"" + testName + "\", test case #" + testCaseNumber + " failed:");
    }


    public static Condition<String> compilationFailure() {
        return containsString("We could not compile the code. See the compilation errors below:");
    }


    public static Condition<String> compilationSuccess() {
        return containsString("--- Compilation\nSuccess");
    }


    public static Condition<String> compilationErrorOnLine(int lineNumber) {
        return containsRegex("- line " + lineNumber + ":\\s");
    }


    public static Condition<String> compilationErrorType(String errorType) {
        return containsRegex("- line \\d+: " + errorType);
    }


    public static Condition<String> failingTestName(String testName) {
        return containsRegex("- Test " + "\"" + testName + "\\(" + "\\)" + "\"" + " failed:");
    }

    public static Condition<String> partiallyCoveredLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {

                int start = value.indexOf("Partially covered lines: ");
                if(start == -1)
                    return false;

                int end = value.indexOf("\n", start);

                String linesAsString = value.substring(start+"Partially covered lines: ".length(), end);
                String[] listOfLines = linesAsString.split(", ");
                return Arrays.stream(listOfLines).anyMatch(l -> l.equals(String.valueOf(line)));
            }
        };
    }

    public static Condition<String> notCoveredLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {

                int start = value.indexOf("Lines not covered: ");
                if(start == -1)
                    return false;

                int end = value.indexOf("\n", start);

                String linesAsString = value.substring(start+"Lines not covered: ".length(), end);
                String[] listOfLines = linesAsString.split(", ");
                return Arrays.stream(listOfLines).anyMatch(l -> l.equals(String.valueOf(line)));
            }
        };
    }


    public static Condition<String> errorType(String errorType) {
        return containsRegex("\\w." + errorType);
    }


    public static Condition<String> errorMessage(String errorMessage) {
        return containsString(errorMessage);
    }


    public static Condition<String> uninvokedMethod(String uninvokedMethod) {
        return containsString("Wanted but not invoked:\n" + uninvokedMethod);
    }


    public static Condition<String> hintAtInteractionFound(String invokedMethod) {
        return containsString("However, there was exactly 1 interaction with this mock:\n" + invokedMethod);
    }

    public static Condition<String> linesCovered(int numberOfLinesCovered) {
        return containsRegex("Line coverage: " + numberOfLinesCovered + "/\\d+");
    }

    public static Condition<String> instructionsCovered(int numberOfInstructionsCovered) {
        return containsRegex("Instruction coverage: " + numberOfInstructionsCovered + "/\\d+");
    }

    public static Condition<String> branchesCovered(int numberOfBranchesCovered) {
        return containsRegex("Branch coverage: " + numberOfBranchesCovered + "/\\d+");
    }

    public static Condition<String> metaTestsPassing(int numberOfMetaTestsPassing) {
        return containsRegex("--- Meta tests\n" + numberOfMetaTestsPassing + "/\\d+ passed");
    }

    public static Condition<String> metaTests(int numberOfMetaTests) {
        return containsRegex("--- Meta tests\n\\d+/" + numberOfMetaTests + " passed");
    }

    public static Condition<String> metaTestFailing(String metaTestName) {
        return containsRegex("Meta test: " + metaTestName + " \\(.*\\) FAILED");
    }

    public static Condition<String> metaTestPassing(String metaTestName) {
        return containsRegex("Meta test: " + metaTestName + " \\(.*\\) PASSED");
    }

    public static Condition<String> finalGrade(String workDir, int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                // the message in the output string is correct
                boolean messageIsCorrect = value.contains("--- Final grade\n" + score + "/100");

                // result xml contains the correct score
                boolean resultXmlIsCorrect = resultXmlHasCorrectGrade(workDir, score);

                // assert passes if both are correct
                return messageIsCorrect && resultXmlIsCorrect;
            }
        };
    }

    public static boolean resultXmlHasCorrectGrade(String workDir, int score) {
        File resultXml = new File(FilesUtils.concatenateDirectories(workDir, "results.xml"));
        String resultXmlContent = FilesUtils.readFile(resultXml);

        int passes = StringUtils.countMatches(resultXmlContent, "<testcase/>");
        int fails = StringUtils.countMatches(resultXmlContent, "<testcase><failure></failure></testcase>");
        boolean resultXmlIsCorrect = passes == score && passes+fails==100;
        return resultXmlIsCorrect;
    }

    public static Condition<String> mutationScore(int mutantsKilled, int totalMutants) {
        return containsString("--- Mutation testing\n" + mutantsKilled + "/" + totalMutants);
    }

    public static Condition<String> allTestsNeedToPassMessage() {
        return containsString("You must ensure that all tests are passing");
    }

    public static Condition<String> scoreOfCodeChecks(int points, int total) {
        return containsString(points + "/" + total);
    }

    public static Condition<String> codeCheck(String description, boolean pass, int weight) {
        String expectedCheck = String.format("%s: %s (weight: %d)",
                description,
                pass ? "PASS" : "FAIL",
                weight);

        return containsString(expectedCheck);
    }

    public static Condition<String> codeCheckScores() {
        return containsRegex("Code checks score: \\d*/\\d*");
    }

    public static Condition<String> failDueToBadConfigurationMessage() {
        return containsString("There might be a problem with this exercise.");
    }
  
    public static Condition<String> totalTimeItTookToExecute() {
        return containsRegex("Andy took \\d+(.\\d)? seconds to assess your question.");
    }


    public static Condition<String> consoleOutput (String output){
        return containsString(output);
    }

    public static Condition<String> consoleOutputExists () {
        return containsString("- Console output");
    }

    public static Condition<String> weDoNotSeeTestsMessage () {
        return containsString("--- Warning\n" +
                "We do not see any tests.\n" +
                "Please check for the following JUnit pre-conditions:");
    }

    public static Condition<String> noMethodSourceProvidedMessage () {
        return containsString("Make sure you have provided a @MethodSource for this @ParameterizedTest!");
    }


    public static Condition<String> hintAtNonStaticMethodSource(String methodSource) {
        return containsString("Make sure your corresponding method " + methodSource
                + "() is static!");
    }


    public static Condition<String> mode(String mode) {
        return containsRegex(String.format("Andy is running in %s mode.", mode));
    }

    public static Condition<String> noMetaTests() {
        return not(containsRegex("--- Meta tests"));
    }

    public static Condition<String> noCodeChecks() {
        return not(containsRegex("--- Code checks"));
    }

    public static Condition<String> noFinalGrade() {
        return not(containsRegex("--- Final grade"));
    }

    public static Condition<String> noJacocoCoverage() {
        return not(containsRegex("--- JaCoCo coverage"));
    }

    public static Condition<String> noPitestCoverage() {
        return not(containsRegex("--- Mutation testing"));
    }

    public static Condition<String> asciiArtPrinted (String asciiArt) {
        return containsString(asciiArt);
    }

}

