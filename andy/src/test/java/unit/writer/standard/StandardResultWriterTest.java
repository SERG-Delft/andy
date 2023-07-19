package unit.writer.standard;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.*;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import testutils.ResultTestDataBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static unit.writer.standard.StandardResultTestAssertions.*;

public class StandardResultWriterTest {
    protected Context ctx = mock(Context.class);
    protected VersionInformation versionInformation = new VersionInformation("testVersion", "testBuildTimestamp", "testCommitId");
    protected RandomAsciiArtGenerator asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
    protected CodeSnippetGenerator codeSnippetGenerator = mock(CodeSnippetGenerator.class);
    protected ResultWriter writer;

    protected ResultWriter buildWriter() {
        return new StandardResultWriter(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    @TempDir
    protected Path reportDir;

    @BeforeEach
    void setupMocks() throws FileNotFoundException {
        DirectoryConfiguration dirs = new DirectoryConfiguration(null, reportDir.toString());
        when(ctx.getDirectoryConfiguration()).thenReturn(dirs);
        when(asciiArtGenerator.getRandomAsciiArt()).thenReturn("random ascii art");
        when(codeSnippetGenerator.generateCodeSnippetFromSolution(any(), anyInt())).thenReturn("arbitrary code snippet");
    }

    @BeforeEach
    void createWriter() {
        this.writer = buildWriter();
    }

    protected String generatedResult() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "stdout.txt")));
    }


    @Test
    void reportCompilationError() throws FileNotFoundException {
        Result result = new ResultTestDataBuilder().withCompilationFail(
                new CompilationErrorInfo("Library.java", 10, "some compilation error"),
                new CompilationErrorInfo("Library.java", 11, "some other compilation error")
        ).build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(not(compilationSuccess()))
                .has(compilationFailure())
                .has(compilationErrorOnLine(10))
                .has(compilationErrorOnLine(11))
                .has(compilationErrorType("some compilation error"))
                .has(compilationErrorType("some other compilation error"))
                .containsOnlyOnce("arbitrary code snippet");

        verify(codeSnippetGenerator, times(1)).generateCodeSnippetFromSolution(ctx, 10);
        verifyNoMoreInteractions(codeSnippetGenerator);
    }

    @Test
    void reportCompilationErrorWithConfigurationError() {
        Result result = new ResultTestDataBuilder()
                .withCompilationFail(
                        new CompilationErrorInfo("SomeConfiguration.java", 10, "some compilation error")
                )
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(not(compilationSuccess()))
                .has(compilationFailure())
                .has(compilationFailureConfigurationError())
                .doesNotContain("arbitrary code snippet");

        verifyNoInteractions(codeSnippetGenerator);
    }

    @Test
    void reportGenericFailureMessage() {
        Result result = new ResultTestDataBuilder()
                .withGenericFailure("test failure")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(genericFailure("test failure"));
    }

    @Test
    void reportGenericFailureStep() {
        String testStep = "TestStep";

        Result result = new ResultTestDataBuilder()
                .withGenericFailureStep(testStep)
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(genericFailure(testStep));
    }

    @Test
    void reportGenericFailureException() {
        String ex = "test exception";

        Result result = new ResultTestDataBuilder()
                .withGenericFailureExceptionMessage(ex)
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(genericFailure(ex));
    }

    @Test
    void reportGenericFailureExternalProcessExitCode() {
        Result result = new ResultTestDataBuilder()
                .withGenericFailureExternalProcessExitCode(1)
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(genericFailure("exit code 1"))
                .doesNotContain("Error message:");
    }

    @Test
    void reportGenericFailureExternalProcessExitCodeAndErrors() {
        Result result = new ResultTestDataBuilder()
                .withGenericFailureExternalProcessExitCode(1)
                .withGenericFailureExternalProcessErrorMessages("test error")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(genericFailure("exit code 1"))
                .has(genericFailure("test error"))
                .contains("Error message:");
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

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(0))
                .has(noFinalGrade())
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(partiallyCoveredLine(4))
                .has(notCoveredLine(5))
                .has(notCoveredLine(6));
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
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescriptionDisplayed("Branch coverage", 1, 2, 0.25))
                .has(fullGradeDescriptionDisplayed("Mutation coverage", 5, 6, 0.25))
                .has(fullGradeDescriptionDisplayed("Code checks", 3, 4, 0.25))
                .has(fullGradeDescriptionDisplayed("Meta tests", 2, 3, 0.25))
                .has(mutationScore(5, 6))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noPenaltyCodeChecks())
                .has(noPenalty())
                .has(not(zeroScoreExplanation()))
                .doesNotContain("test success message");

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();

        verifyNoInteractions(codeSnippetGenerator);
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
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescriptionDisplayed("Branch coverage", 1, 2, 1))
                .doesNotContain("Mutation coverage")
                .doesNotContain("Code checks")
                .doesNotContain("Meta tests")
                .has(mutationScore(5, 6))
                .has(noMetaTests())
                .doesNotContain("test success message");

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
        when(modeActionSelector.getAction()).thenReturn(Action.TESTS);

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
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(34))
                .has(noFinalGrade())
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(not(fullGradeDescription("Branch coverage", 1, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 5, 6, 0.25)))
                .has(not(fullGradeDescription("Code checks", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .has(mutationScore(5, 6))
                .has(zeroScoreExplanation())
                .contains("only checking if your tests pass")
                .doesNotContain("test success message");

        verify(asciiArtGenerator, times(0)).getRandomAsciiArt();
    }

    @Test
    void zeroWeightMeansComponentIsNotShown() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(false);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);
        when(modeActionSelector.getAction()).thenReturn(Action.TESTS);

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
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeNotOnScreen(34))
                .has(noFinalGrade())
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(not(fullGradeDescription("Branch coverage", 1, 2, 0.25)))
                .has(not(fullGradeDescription("Mutation coverage", 5, 6, 0.25)))
                .has(not(fullGradeDescription("Code checks", 3, 4, 0.25)))
                .has(not(fullGradeDescription("Meta tests", 2, 3, 0.25)))
                .has(mutationScore(5, 6))
                .has(zeroScoreExplanation())
                .contains("only checking if your tests pass")
                .doesNotContain("test success message");

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
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(0))
                .has(not(compilationSuccess()))
                .has(compilationFailure())
                .has(noCodeChecks())
                .has(noJacocoCoverage())
                .has(noCodeChecks())
                .has(noMetaTests())
                .has(noPitestCoverage())
                .has(not(zeroScoreExplanation()))
                .doesNotContain("test success message");

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

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(100))
                .has(not(zeroScoreExplanation()))
                .contains("random ascii art");
    }

    @Test
    void testPrintFinalGradeWithScore100AndSuccessMessage() {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(false);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(false);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withGrade(100)
                .withSuccessMessage("test success message")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(100))
                .has(not(zeroScoreExplanation()))
                .contains("random ascii art")
                .contains("test success message");
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
                .withPenaltyCodeCheckResults(List.of(
                        new CodeCheckResult("a1", 1, true),
                        new CodeCheckResult("b1", 1, true),
                        new CodeCheckResult("c1", 100, true),
                        new CodeCheckResult("d1", 5, true)
                ))
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescriptionDisplayed("Branch coverage", 1, 2, 0.25))
                .has(fullGradeDescriptionDisplayed("Mutation coverage", 5, 6, 0.25))
                .has(fullGradeDescriptionDisplayed("Code checks", 3, 4, 0.25))
                .has(fullGradeDescriptionDisplayed("Meta tests", 2, 3, 0.25))
                .has(mutationScore(5, 6))
                .has(scoreOfCodeChecks(7, 8))
                .has(metaTestsPassing(2))
                .has(metaTests(3))
                .has(noPenalty())
                .has(not(zeroScoreExplanation()));

        assertThat(output)
                .has(metaTestDisplayed("d", true, fullHints))
                .has(metaTestDisplayed("e", false, fullHints))
                .has(metaTestDisplayed("f", true, fullHints))
                .has(codeCheckDisplayed("a", true, 1, fullHints))
                .has(codeCheckDisplayed("b", true, 2, fullHints))
                .has(codeCheckDisplayed("c", false, 1, fullHints))
                .has(penaltyCodeCheckDisplayed("a1", true, 1, fullHints))
                .has(penaltyCodeCheckDisplayed("b1", true, 1, fullHints))
                .has(penaltyCodeCheckDisplayed("c1", true, 100, fullHints))
                .has(penaltyCodeCheckDisplayed("d1", true, 5, fullHints));
    }

    @ParameterizedTest
    @CsvSource({
            "true,false",
            "false,true",
            "true,true"
    })
    void testPrintFinalGradeWithPenaltyCodeChecksFailing(boolean fullHints, boolean partialHints) {
        ModeActionSelector modeActionSelector = mock(ModeActionSelector.class);
        when(modeActionSelector.shouldCalculateAndShowGrades()).thenReturn(true);
        when(modeActionSelector.shouldGenerateAnalytics()).thenReturn(false);
        when(modeActionSelector.shouldShowFullHints()).thenReturn(fullHints);
        when(modeActionSelector.shouldShowPartialHints()).thenReturn(partialHints);
        when(modeActionSelector.getMode()).thenReturn(Mode.PRACTICE);

        when(ctx.getModeActionSelector()).thenReturn(modeActionSelector);

        Result result = new ResultTestDataBuilder()
                .withCoverageResult(CoverageResult.build(
                        4, 7, 5, 8, 1, 2,
                        new CoverageLineByLine(List.of(), List.of(), List.of())))
                .withMutationTestingResults(5, 6)
                .withPenaltyCodeCheckResults(List.of(
                        new CodeCheckResult("a1", 1, false),
                        new CodeCheckResult("b1", 100, false),
                        new CodeCheckResult("c1", 50, true),
                        new CodeCheckResult("d1", 1, true)
                ))
                .withPenalty(101)
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(compilationSuccess())
                .has(fullGradeDescriptionDisplayed("Code checks", 0, 0, 0.25))
                .has(mutationScore(5, 6))
                .has(penalty(101))
                .has(codeChecks())
                .has(scoreOfCodeChecks(2, 4))
                .has(not(zeroScoreExplanation()));

        assertThat(output)
                .has(penaltyCodeCheckDisplayed("a1", false, 1, fullHints))
                .has(penaltyCodeCheckDisplayed("b1", false, 100, fullHints))
                .has(penaltyCodeCheckDisplayed("c1", true, 50, fullHints))
                .has(penaltyCodeCheckDisplayed("d1", true, 1, fullHints));
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
                .withWeights(0.5f, 0.5f, 0, 0)
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(finalGradeOnScreen(34))
                .has(compilationSuccess())
                .has(linesCovered(4))
                .has(instructionsCovered(5))
                .has(branchesCovered(1))
                .has(fullGradeDescriptionDisplayed("Branch coverage", 1, 2, 0.5))
                .has(fullGradeDescriptionDisplayed("Mutation coverage", 5, 6, 0.5))
                .has(mutationScore(5, 6))
                .doesNotContain("Code checks")
                .doesNotContain("Meta tests")
                .has(not(zeroScoreExplanation()));
    }

    @Test
    void testPrintTestResultsWithNoFailingMessageOrConsoleOutput() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(5, 4, 3,
                        List.of(),
                        "")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(5))
                .has(not(allTestsNeedToPassMessage()))
                .has(not(consoleOutputExists()));
    }

    @Test
    void testPrintTestResultsWithConsoleOutput() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(5, 4, 3,
                        List.of(),
                        "test console output")
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(5))
                .has(not(allTestsNeedToPassMessage()))
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

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(compilationSuccess())
                .has(testResults())
                .has(numberOfJUnitTestsPassing(1))
                .has(totalNumberOfJUnitTests(5))
                .has(allTestsNeedToPassMessage())
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

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(versionInformation(versionInformation))
                .has(compilationSuccess())
                .has(testResults())
                .has(noJUnitTestsFound())
                .has(not(allTestsNeedToPassMessage()))
                .has(not(numberOfJUnitTestsPassing(0)))
                .has(not(totalNumberOfJUnitTests(0)))
                .has(not(consoleOutputExists()));
    }

    @Test
    void testPrintTestResultsWithNoTestsFoundDueToTestFailure() {
        Result result = new ResultTestDataBuilder()
                .withTestResults(0, 0, 0,
                        List.of(new TestFailureInfo("exampleTestCase", "example failure")),
                        "")
                .withCompilationFail()
                .build();

        writer.write(ctx, result);

        String output = generatedResult();

        assertThat(output)
                .has(compilationSuccess())
                .has(testResults())
                .has(not(noJUnitTestsFound()))
                .has(allTestsNeedToPassMessage())
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(0))
                .contains("- exampleTestCase:\nexample failure");
    }

    @Test
    void uncaughtError() {
        Exception ex = new RuntimeException("Some exception");

        writer.uncaughtError(ctx, ex);

        String output = generatedResult();

        assertThat(output)
                .has(unexpectedError())
                .contains(ex.getClass().getName())
                .contains(ex.getMessage());
    }

    protected Condition<? super String> finalGradeOnScreen(int grade) {
        return StandardResultTestAssertions.finalGrade(grade);
    }

    protected Condition<? super String> finalGradeNotOnScreen(int grade) {
        return not(StandardResultTestAssertions.finalGrade(grade));
    }

    protected Condition<? super String> fullGradeDescriptionDisplayed(String check, int scored, int total, double weight) {
        return fullGradeDescription(check, scored, total, weight);
    }

    protected Condition<? super String> codeCheckDisplayed(String description, boolean pass, int weight, boolean shownInOutput) {
        var codeCheckCondition = codeCheck(description, pass, weight);
        return shownInOutput ? codeCheckCondition : not(codeCheckCondition);
    }

    protected Condition<? super String> penaltyCodeCheckDisplayed(String description, boolean pass, int weight, boolean shownInOutput) {
        var codeCheckCondition = penaltyCodeCheck(description, pass, weight);
        return shownInOutput ? codeCheckCondition : not(codeCheckCondition);
    }

    protected Condition<? super String> metaTestDisplayed(String description, boolean pass, boolean shownInOutput) {
        var metaTestCondition = pass ? metaTestPassing(description) : metaTestFailing(description);
        return shownInOutput ? metaTestCondition : not(metaTestCondition);
    }

}
