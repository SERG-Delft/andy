package unit.writer.weblab;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.*;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import testutils.ResultTestDataBuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.Mockito.*;
import static testutils.WebLabHighlightsJsonTestAssertions.*;
import static testutils.WebLabTestAssertions.*;

public class WebLabResultWriterTest {

    private Context ctx = mock(Context.class);
    private RandomAsciiArtGenerator asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
    private WebLabResultWriter writer = new WebLabResultWriter(ctx, asciiArtGenerator);

    @TempDir
    protected Path reportDir;

    @BeforeEach
    void setupMocks() {
        DirectoryConfiguration dirs = new DirectoryConfiguration("any", reportDir.toString());
        when(ctx.getDirectoryConfiguration()).thenReturn(dirs);
        when(asciiArtGenerator.getRandomAsciiArt()).thenReturn("random ascii art");
    }

    private String generatedResult() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "stdout.txt")));
    }

    private String highlightsJson() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "highlights.json")));
    }

    @Test
    void reportCompilationError() {
        Result result = new ResultTestDataBuilder().withCompilationFail(
                new CompilationErrorInfo("Library.java", 10, "some compilation error"),
                new CompilationErrorInfo("Library.java", 11, "some other compilation error")
        ).build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGradeInXml(reportDir.toString(), 0))
                .has(compilationFailure())
                .has(compilationErrorOnLine(10))
                .has(compilationErrorOnLine(11))
                .has(compilationErrorType("some compilation error"))
                .has(compilationErrorType("some other compilation error"));

        String highlightsJson = highlightsJson();

        assertThat(highlightsJson)
                .has(highlightCompilationError(10, "some compilation error"))
                .has(highlightCompilationError(11, "some other compilation error"));
    }

    @Test
    void reportCompilationErrorWithConfigurationError() {
        Result result = new ResultTestDataBuilder()
                .withCompilationFail(
                        new CompilationErrorInfo("SomeConfiguration.java", 10, "some compilation error")
                )
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationFailure())
                .has(compilationFailureConfigurationError());
    }

    @Test
    void reportGenericFailure() {
        Result result = new ResultTestDataBuilder()
                .withGenericFailure("test failure")
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGradeInXml(reportDir.toString(), 0))
                .has(genericFailure("test failure"));
    }

    @Test
    void testLineCoverage() {
        Result result = new ResultTestDataBuilder()
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(
                                List.of(1, 2, 3, 7),
                                List.of(4),
                                List.of(5, 6)
                        )
                ))
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGradeInXml(reportDir.toString(), 0))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(partiallyCoveredLine(4))
                .has(notCoveredLine(5))
                .has(notCoveredLine(6));

        String highlightsJson = highlightsJson();

        assertThat(highlightsJson)
                .has(highlightLineFullyCovered(1))
                .has(highlightLineFullyCovered(2))
                .has(highlightLineFullyCovered(3))
                .has(highlightLineFullyCovered(7))
                .has(highlightLinePartiallyCovered(4))
                .has(highlightLineNotCovered(5))
                .has(highlightLineNotCovered(6));
    }

    @Test
    void testPrintFinalGrade() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(34)
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withMutationTestingResults(5, 6)
                .withCodeCheckResults(List.of(
                        new CodeCheckResult("a", 1, true),
                        new CodeCheckResult("b", 2, true),
                        new CodeCheckResult("c", 1, false)
                ))
                .withMetaTestResults(List.of(
                        new MetaTestResult("d", 1, true),
                        new MetaTestResult("e", 3, false),
                        new MetaTestResult("f", 1, true)
                ))
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescription("Branch coverage", 1, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 5, 6, 0.25))
                .has(fullGradeDescription("Code checks", 3, 4, 0.25))
                .has(fullGradeDescription("Meta tests", 2, 3, 0.25))
                .has(mutationScore(5, 6))
                .has(noMetaTests())
                .has(noCodeChecks());

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();
    }

    @Test
    void testPrintFinalGradeWithWeight0() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(34)
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withMutationTestingResults(5, 6)
                .withWeights(1, 0, 0, 0)
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescription("Branch coverage", 1, 2, 1))
                .has(fullGradeDescription("Mutation coverage", 5, 6, 0))
                .has(fullGradeDescription("Code checks", 0, 0, 0))
                .has(fullGradeDescription("Meta tests", 0, 0, 0))
                .has(mutationScore(5, 6))
                .has(noMetaTests())
                .has(noCodeChecks());

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();
    }

    @Test
    void testPrintFinalGradeWithoutCalculatingGrades() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(false);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(34)
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withMutationTestingResults(5, 6)
                .withCodeCheckResults(List.of(
                        new CodeCheckResult("a", 1, true),
                        new CodeCheckResult("b", 2, true),
                        new CodeCheckResult("c", 1, false)
                ))
                .withMetaTestResults(List.of(
                        new MetaTestResult("d", 1, true),
                        new MetaTestResult("e", 3, false),
                        new MetaTestResult("f", 1, true)
                ))
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGradeInXml(reportDir.toString(), 34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(not(fullGradeDescription("Branch coverage", 1, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 5, 6, 0.25)))
                .has(not(fullGradeDescription("Code checks", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .has(mutationScore(5, 6));

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();
    }

    @Test
    void testPrintFinalGradeWithCompilationError() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(0)
                .withCompilationFail(new CompilationErrorInfo("a", 1, "a"))
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 0))
                .has(compilationFailure())
                .has(noCodeChecks())
                .has(noJacocoCoverage())
                .has(noCodeChecks())
                .has(noMetaTests())
                .has(noPitestCoverage());

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();
    }

    @Test
    void testPrintFinalGradeWithScore100() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(100)
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 100))
                .contains("random ascii art");
    }

    @ParameterizedTest
    @CsvSource({
            "true,false",
            "false,true",
            "true,true"
    })
    void testPrintFinalGradeWithCodeChecksAndMetaTestsDisplayed(boolean fullHints, boolean partialHints) {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(fullHints);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(partialHints);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(34)
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withMutationTestingResults(5, 6)
                .withCodeCheckResults(List.of(
                        new CodeCheckResult("a", 1, true),
                        new CodeCheckResult("b", 2, true),
                        new CodeCheckResult("c", 1, false)
                ))
                .withMetaTestResults(List.of(
                        new MetaTestResult("d", 1, true),
                        new MetaTestResult("e", 3, false),
                        new MetaTestResult("f", 1, true)
                ))
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescription("Branch coverage", 1, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 5, 6, 0.25))
                .has(fullGradeDescription("Code checks", 3, 4, 0.25))
                .has(fullGradeDescription("Meta tests", 2, 3, 0.25))
                .has(mutationScore(5, 6))
                .has(scoreOfCodeChecks(3, 4))
                .has(metaTestsPassing(2))
                .has(metaTests(3));

        if (fullHints) {
            assertThat(output)
                    .has(metaTestPassing("d"))
                    .has(metaTestFailing("e"))
                    .has(metaTestPassing("f"))
                    .has(codeCheck("a", true, 1))
                    .has(codeCheck("b", true, 2))
                    .has(codeCheck("c", false, 1));
        }
    }

    @Test
    void testPrintFinalGradeWithCodeChecksAndMetaTestsDisplayedButNoCodeChecksOrTests() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(true);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(true);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(34)
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withCodeCheckResults(List.of())
                .withMetaTestResults(List.of())
                .withMutationTestingResults(5, 6)
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(finalGrade(reportDir.toString(), 34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescription("Branch coverage", 1, 2, 0.25))
                .has(fullGradeDescription("Mutation coverage", 5, 6, 0.25))
                .has(fullGradeDescription("Code checks", 0, 0, 0.25))
                .has(fullGradeDescription("Meta tests", 0, 0, 0.25))
                .has(mutationScore(5, 6))
                .has(noCodeChecksToBeAssessed())
                .has(metaTests(0));
    }

    @Test
    void testPrintTestResultsWithNoFailingMessageOrConsoleOutput() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(5, 4, 3,
                        List.of(),
                        "")
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(5))
                .has(not(consoleOutputExists()));
    }

    @Test
    void testPrintTestResultsWithConsoleOutput() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(5, 4, 3,
                        List.of(),
                        "test console output")
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(5))
                .has(consoleOutputExists())
                .has(consoleOutput("test console output"));
    }

    @Test
    void testPrintTestResultsWithFailingMessages() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(5, 4, 1,
                        List.of(
                                new TestFailureInfo("test case 1", ""),
                                new TestFailureInfo("test case 2", "test message"),
                                new TestFailureInfo("test case 3", null)
                        ),
                        null)
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(1))
                .has(totalNumberOfJUnitTests(5))
                .has(jUnitTestFailing("test case 1", ""))
                .has(jUnitTestFailing("test case 2", "test message"))
                .has(jUnitTestFailing("test case 3", ""))
                .has(not(consoleOutputExists()));
    }

    @Test
    void testPrintTestResultsWithNoTestsFound() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(0, 0, 0,
                        List.of(),
                        "test console output")
                .build();

        writer.write(result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationSuccess())
                .has(testResults())
                .has(noJUnitTestsFound())
                .has(not(numberOfJUnitTestsPassing(0)))
                .has(not(totalNumberOfJUnitTests(0)))
                .has(not(consoleOutputExists()));
    }

    @Test
    void uncaughtError() {
        Exception ex = new RuntimeException("Some exception");

        writer.uncaughtError(ex);

        String output = generatedResult();

        assertThat(output)
                .has(unexpectedError())
                .contains(ex.getClass().getName())
                .contains(getClass().getName())
                .contains(ex.getMessage());
    }

    // TODO: Tests for writeAnalyticsFile when it is completed

}
