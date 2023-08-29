package unit.writer.standard;

import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.assertj.core.api.Condition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.AssertionsForClassTypes.not;


public class StandardResultTestAssertions {

    protected static Condition<String> containsRegex(String regex) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(value);
                return matcher.find();
            }
        };
    }

    public static Condition<String> containsString(String str) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains(str);
            }
        };
    }

    public static Condition<String> startsWithString(String str) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.startsWith(str);
            }
        };
    }

    public static Condition<String> numberOfJUnitTestsPassing(int numberOfTestsPassing) {
        return containsRegex("--- JUnit execution\\n" + numberOfTestsPassing + "\\/\\d+ passed");
    }

    public static Condition<String> testResults() {
        return containsString("--- JUnit execution");
    }

    public static Condition<String> totalNumberOfJUnitTests(int numberOfTests) {
        return containsRegex("--- JUnit execution\\n\\d+\\/" + numberOfTests + " passed");
    }

    public static Condition<String> jUnitTestFailing(String testName, String message) {
        return containsRegex("- " + testName + ":\n" + message);
    }

    public static Condition<String> noJUnitTestsFound() {
        return containsString("Warning\nWe do not see any tests.");
    }

    public static Condition<String> unexpectedError() {
        return containsString("Something unexpected just happened");
    }

    public static Condition<String> compilationFailure() {
        return containsString("We could not compile the code. See the compilation errors below:");
    }

    public static Condition<String> compilationFailureConfigurationError() {
        return containsString("There might be a problem with this exercise");
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

    public static Condition<String> versionInformation(String version, String commitId, String timestamp) {
        return containsString(String.format("Andy v%s-%s (%s)\n", version, commitId, timestamp));
    }

    public static Condition<String> versionInformation(VersionInformation versionInformation) {
        return versionInformation(versionInformation.getVersion(),
                versionInformation.getCommitId(),
                versionInformation.getBuildTimestamp());
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
    public static Condition<String> penaltyMetaTestFailing(String metaTestName, int penalty) {
        return containsRegex("Penalty meta test: " + metaTestName + " \\(.*" + penalty +".*\\) FAILED");
    }

    public static Condition<String> penaltyMetaTestPassing(String metaTestName, int penalty) {
        return containsRegex("Penalty meta test: " + metaTestName + " \\(.*" + penalty +".*\\) PASSED");
    }

    public static Condition<String> finalGrade(int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                // the message in the output string is correct
                boolean messageIsCorrect = value.contains("Final grade: " + score + "/100");

                return messageIsCorrect;
            }
        };
    }

    public static Condition<String> penalty(int penalty) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                // the message in the output string is correct
                boolean messageIsCorrect = value.contains("Penalty: " + penalty);

                return messageIsCorrect;
            }
        };
    }

    public static Condition<String> noPenalty() {
        return not(containsString("Penalty: "));
    }

    public static Condition<String> finalGradeInXml(String reportDir, int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return resultXmlHasCorrectGrade(reportDir, score);
            }
        };
    }

    public static Condition<String> metaScoreInXml(String reportDir, String description, int score) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return resultXmlHasMetaScore(reportDir, description, score);
            }
        };
    }

    private static Document parseResultsXmlDocument(String workDir) {
        Document doc;
        try {
            FileInputStream fileIS = new FileInputStream(FilesUtils.concatenateDirectories(workDir, "results.xml"));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            doc = builder.parse(fileIS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private static boolean resultXmlHasCorrectGrade(String workDir, int score) {
        Document doc = parseResultsXmlDocument(workDir);

        int passes = getXmlWeight(doc, "/testsuites/testsuite/testcase[@name=\"Passed\" and not(failure)]");
        int fails = getXmlWeight(doc, "/testsuites/testsuite/testcase[@name=\"Failed\" and failure]");
        boolean resultXmlIsCorrect = passes == score && passes + fails == 100;
        return resultXmlIsCorrect;
    }

    private static boolean resultXmlHasMetaScore(String workDir, String description, int score) {
        Document doc = parseResultsXmlDocument(workDir);

        final String xpath = "/testsuites/meta/score[@id=\"%s\"]".formatted(description);
        try {
            Element node = (Element) XPathFactory.newInstance().newXPath().compile(xpath)
                    .evaluate(doc, XPathConstants.NODE);
            int actualScore = Integer.parseInt(node.getTextContent());
            return score == actualScore;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static int getXmlWeight(Document doc, String xpath) {
        try {
            Element node = (Element) XPathFactory.newInstance().newXPath().compile(xpath)
                    .evaluate(doc, XPathConstants.NODE);
            return Integer.parseInt(node.getAttribute("weight"));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static Condition<String> mutationScore(int mutantsKilled, int totalMutants) {
        return containsString("--- Mutation testing\n" + mutantsKilled + "/" + totalMutants);
    }

    public static Condition<String> allTestsNeedToPassMessage() {
        return containsString("You must ensure that all tests are passing");
    }

    public static Condition<String> scoreOfCodeChecks(int points, int total) {
        return containsString("--- Code checks\n"+points + "/" + total + " passed");
    }

    public static Condition<String> codeCheck(String description, boolean pass, int weight) {
        String expectedCheck = String.format("Code check %s (weight: %d): %s",
                description,
                weight,
                pass ? "PASSED" : "FAILED");

        return containsString(expectedCheck);
    }

    public static Condition<String> penaltyCodeCheck(String description, boolean pass, int weight) {
        String expectedCheck = String.format("Penalty code check %s (penalty: %d): %s",
                description,
                weight,
                pass ? "PASS" : "FAIL");

        return containsString(expectedCheck);
    }

    public static Condition<String> totalTimeItTookToExecute() {
        return containsRegex("took \\d+(.\\d)? seconds");
    }

    public static Condition<String> consoleOutput (String output){
        return containsString(output);
    }

    public static Condition<String> consoleOutputExists () {
        return containsString("- Console output");
    }

    public static Condition<String> fullGradeDescription(String check, int scored, int total, double weight) {
        return containsString(String.format("%s: %d/%d (overall weight=%.2f)%s", check, scored, total, weight,
                (total > 0 && weight == 0 ? " (0 gives full points)" : "")));
    }

    public static Condition<String> mode(String mode) {
        return containsRegex(String.format("Andy is running in %s mode.", mode));
    }

    public static Condition<String> noMetaTests() {
        return not(containsRegex("--- Meta tests"));
    }
    public static Condition<String> noPenaltyMetaTests() {
        return not(containsRegex(".*Penalty meta test.*\\(penalty: \\d+\\)"));
    }

    public static Condition<String> codeChecks() {
        return containsString("--- Code checks");
    }

    public static Condition<String> noCodeChecks() {
        return not(codeChecks());
    }

    public static Condition<String> noPenaltyCodeChecks() {
        return not(containsRegex(".*Penalty code check.*\\(penalty: \\d+\\)"));
    }

    public static Condition<String> noFinalGrade() {
        return not(containsRegex("--- Assessment"));
    }

    public static Condition<String> zeroScoreExplanation() {
        return containsRegex("Final test score is (shown as )?0/100");
    }

    public static Condition<String> noJacocoCoverage() {
        return not(containsRegex("--- JaCoCo coverage"));
    }

    public static Condition<String> noPitestCoverage() {
        return not(containsRegex("--- Mutation testing"));
    }

    public static Condition<String> genericFailure(String failure) {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains("we are facing a failure") && value.contains(failure);
            }
        };
    }

    public static Condition<String> flakyTestSuiteMessage() {
        return new Condition<>() {
            @Override
            public boolean matches(String value) {
                return value.contains("It appears that your test suite is flaky.");
            }
        };
    }

}

