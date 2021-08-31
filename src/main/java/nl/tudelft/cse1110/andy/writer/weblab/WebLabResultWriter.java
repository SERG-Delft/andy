package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.*;
import nl.tudelft.cse1110.andy.utils.ImportUtils;
import nl.tudelft.cse1110.andy.writer.ResultWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.writeToFile;

public class WebLabResultWriter implements ResultWriter {
    private final Context ctx;
    private final RandomAsciiArtGenerator asciiArtGenerator;

    private StringBuilder toDisplay = new StringBuilder();
    private List<Highlight> highlights = new ArrayList<>();

    public WebLabResultWriter(Context ctx, RandomAsciiArtGenerator asciiArtGenerator) {
        this.ctx = ctx;
        this.asciiArtGenerator = asciiArtGenerator;
    }

    @Override
    public void write(Result result) {
        writeStdOutFile(result);
        writeResultsXmlFile(result);
        writeHighlightsJson(result);
        writeAnalyticsFile(result);
    }

    private void writeStdOutFile(Result result) {

        if(result.hasGenericFailure()) {
            toDisplay.append(result.getGenericFailure());
        } else {
            printCompilationResult(result.getCompilation());
            printTestResults(result.getTests());
            printCoverageResults(result.getCoverage());
            printMutationTestingResults(result.getMutationTesting());
            printCodeCheckResults(result.getCodeChecks());
            printMetaTestResults(result.getMetaTests());
            printFinalGrade(result);
            printModeAndTimeToRun(result.getTimeInSeconds());
        }

        File stdoutTxt = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "stdout.txt"));
        writeToFile(stdoutTxt, toDisplay.toString());
    }

    private void writeResultsXmlFile(Result result) {
        String xml = buildResultsXml(result);

        File resultsXml = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "results.xml"));
        writeToFile(resultsXml, xml);
    }

    private String buildResultsXml(Result result) {
        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n\t<testsuite>\n");
        int score = result.getFinalGrade();
        String failed = "\t\t<testcase><failure></failure></testcase>\n";
        String passed = "\t\t<testcase/>\n";

        //score = -1 means compilation error
        if(score >= 0){
            xml.append(failed.repeat(100 - score));
            xml.append(passed.repeat(score));
        } else{
            xml.append(failed.repeat(100));
        }

        xml.append("\t</testsuite>\n</testsuites>\n");
        return xml.toString();
    }


    private void writeHighlightsJson(Result result){

        List<Highlight> highlights = buildHighlights(result);
        String json = new Gson().toJson(highlights);

        File highlightsJson = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "highlights.json"));
        writeToFile(highlightsJson, json);
    }

    private List<Highlight> buildHighlights(Result result) {
        List<Highlight> highlights = new ArrayList<>();

        // coverage
        for(int line : result.getCoverage().getFullyCoveredLines()) {
            highlights.add(new Highlight(line, "100% coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.FULL_COVERAGE));
        }

        for(int line : result.getCoverage().getPartiallyCoveredLines()) {
            highlights.add(new Highlight(line, "Partial coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.PARTIAL_COVERAGE));
        }

        for(int line : result.getCoverage().getNotCoveredLines()) {
            highlights.add(new Highlight(line, "No coverage",
                    Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.NO_COVERAGE));
        }

        // compilation error
        for (CompilationErrorInfo error : result.getCompilation().getErrors()) {
            highlights.add(new Highlight(error.getLineNumber(), error.getMessage(), Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
        }

        return highlights;
    }

    private void writeAnalyticsFile(Result result) {
        Submission submission = new Submission(
                new SubmissionMetaData("course", "studentid", "studentname", "exercise"),
                result
        );

        String json = new Gson().toJson(submission);

        File file = new File(concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "post.json"));
        writeToFile(file, json);
    }


    private void printFinalGrade(Result result) {
        // we only show grades in specific modes and actions
        // if ModeActionSelector is not injected yet (i.e., it's null), it's because compilation fail.
        // in this case, we give it a zero, no matter the mode.
        boolean shouldShowGrades = modeActionSelector()!=null && modeActionSelector().shouldCalculateAndShowGrades();
        if(!shouldShowGrades)
            return;

        int finalGrade = result.getFinalGrade();

        l("");
        l("--- Assessment");

        // describe the weights and grades per component
        if(finalGrade > 0) {
            printGradeCalculationDetails("Branch coverage", result.getCoverage().getTotalCoveredBranches(), result.getCoverage().getTotalBranches(), result.getWeights().getBranchCoverageWeight());
            printGradeCalculationDetails("Mutation coverage", result.getMutationTesting().getKilledMutants(), result.getMutationTesting().getTotalNumberOfMutants(), result.getWeights().getMutationCoverageWeight());
            printGradeCalculationDetails("Code checks", result.getCodeChecks().getPassedWeightedChecks(), result.getCodeChecks().getTotalWeightedChecks(), result.getWeights().getCodeChecksWeight());
            printGradeCalculationDetails("Meta tests", result.getMetaTests().getPassedMetaTests(), result.getMetaTests().getTotalTests(), result.getWeights().getMetaTestsWeight());
            l("");
        }

        // print the final grade
        l(String.format("Final grade: %d/100", finalGrade));

        // print some nice ascii art if it's full grade!
        boolean fullyCorrect = finalGrade == 100;
        if (fullyCorrect) {
            String randomAsciiArt = asciiArtGenerator.getRandomAsciiArt();
            l("");
            l(randomAsciiArt);
        }
    }

    private void printGradeCalculationDetails(String what, int score, int total, double weight) {
        l(String.format("%s: %d/%d (overall weight=%.2f)%s", what, score, total, weight,
                (total > 0 && weight==0 ? " (0 gives full points)":"") ));
    }

    private void printModeAndTimeToRun(double timeInSeconds) {
        if(modeActionSelector()!=null)
            l(String.format("\nAndy is running in %s mode and took %.1f seconds to assess your question.", modeActionSelector().getMode().toString(), timeInSeconds));
        else
            l(String.format("\nAndy took %.1f seconds to assess your question.", timeInSeconds));
    }

    private void printMetaTestResults(MetaTestsResult metaTests) {
        if(!metaTests.wasExecuted())
            return;

        boolean allHints = modeActionSelector().shouldShowFullHints();
        boolean onlyResult = modeActionSelector().shouldShowPartialHints();

        if(!allHints && !onlyResult)
            return;

        l("\n--- Meta tests");
        l(String.format("%d/%d passed", metaTests.getPassedMetaTests(), metaTests.getTotalTests()));

        if (allHints) {
            for (MetaTestResult metaTestResult : metaTests.getMetaTestResults()) {
                if (metaTestResult.succeeded()) {
                    l(String.format("Meta test: %s (weight: %d) PASSED", metaTestResult.getName(), metaTestResult.getWeight()));
                } else {
                    l(String.format("Meta test: %s (weight: %d) FAILED", metaTestResult.getName(), metaTestResult.getWeight()));
                }
            }
        }

    }

    private void printCoverageResults(CoverageResult coverage) {
        if(!coverage.wasExecuted())
            return;

        l("\n--- JaCoCo coverage");

        l(String.format("Line coverage: %d/%d", coverage.getTotalCoveredLines(), coverage.getTotalLines()));
        l(String.format("Instruction coverage: %d/%d", coverage.getTotalCoveredInstructions(), coverage.getTotalInstructions()));
        l(String.format("Branch coverage: %d/%d", coverage.getTotalCoveredBranches(), coverage.getTotalBranches()));

        if(!coverage.getPartiallyCoveredLines().isEmpty()) {
            l(String.format("Partially covered lines: %s",
                    coverage.getPartiallyCoveredLines().stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }

        if(!coverage.getNotCoveredLines().isEmpty()) {
            l(String.format("Lines not covered: %s",
                    coverage.getNotCoveredLines().stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }

    }

    private void printCodeCheckResults(CodeChecksResult codeChecks) {
        if(!codeChecks.wasExecuted())
            return;

        boolean allHints = modeActionSelector().shouldShowFullHints();
        boolean onlyResult = modeActionSelector().shouldShowPartialHints();

        if(!allHints && !onlyResult)
            return;

        if(!codeChecks.hasChecks()) {
            l("\n--- Code checks");
            l("No code checks to be assessed");
        }
        else {
            l("\n--- Code checks");
            l(String.format("%d/%d passed", codeChecks.getPassedWeightedChecks(), codeChecks.getTotalWeightedChecks()));

            if(allHints) {
                for (CodeCheckResult result : codeChecks.getCheckResults()) {
                    l(String.format("%s: %s (weight: %d)",
                            result.getDescription(),
                            result.passed() ? "PASS" : "FAIL",
                            result.getWeight()));
                }

            }
        }

    }

    private void printMutationTestingResults(MutationTestingResult mutationTesting) {
        if(!mutationTesting.wasExecuted())
            return;

        l("\n--- Mutation testing");
        l(String.format("%d/%d killed", mutationTesting.getKilledMutants(), mutationTesting.getTotalNumberOfMutants()));
    }

    private void printTestResults(UnitTestsResult tests) {

        if(!tests.wasExecuted())
            return;

        l("\n--- JUnit execution");
        if (tests.getTestsFound() == 0) {
            noTestsFound(tests);
        } else {
            l(String.format("%d/%d passed", tests.getTestsSucceeded(), tests.getTestsFound()));

            if(tests.hasFailingMessage()) {
                l("");

                for (TestFailureInfo failure : tests.getFailures()) {
                    l("- " + failure.getTestCase() + ":");
                    if(failure.hasMessage())
                        l(failure.getMessage());
                }

                l("You must ensure that all tests are passing! Stopping the assessment.");
            }

            if(tests.hasConsoleOutput()) {
                l("\n--- Console output");
                l(tests.getConsole());
            }

        }
    }

    private void noTestsFound(UnitTestsResult tests) {
        l(
        "Warning\nWe do not see any tests.\n" +
        "Please check for the following JUnit pre-conditions:\n" +
        "- Normal tests must be annotated with \"@Test\"\n" +
        "- Parameterized tests must be annotated with \"@ParameterizedTest\"\n" +
        "- Method sources must be static and provided as: \"@MethodSource(\"generator\")\" e.g.\n" +
        "- Property based tests must be annotated with \"@Property\"\n" +
        "- @BeforeAll and @AfterAll methods should be static\n" +
        "- @BeforeEach methods should be non-static");
    }

    private void printCompilationResult(CompilationResult compilation) {
        if(compilation.successful()) {
            l("--- Compilation\nSuccess");
        } else {
            l("We could not compile the code. See the compilation errors below:");
            List<CompilationErrorInfo> compilationErrors = compilation.getErrors();

            for(CompilationErrorInfo error : compilationErrors) {

                String message = error.getMessage();
                long lineNumber = error.getLineNumber();
                l(String.format("- line %d: %s",
                        lineNumber,
                        message));

                Optional<String> importLog = ImportUtils.checkMissingImport(message);
                importLog.ifPresent(this::l);

                highlights.add(new Highlight(lineNumber, message, Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
            }

            if(compilation.hasConfigurationError()) {
                l("\n**WARNING:** There might be a problem with this exercise. "+
                        "Please contact the teaching staff so they can fix this as quickly" +
                        "as possible. Thank you for your help! :)");
            }
        }
    }



    private void l(String line) {
        toDisplay.append(line);
        toDisplay.append("\n");
    }

    private ModeActionSelector modeActionSelector() {
        return ctx.getModeActionSelector();
    }


}
