package integration;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.step.*;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static unit.writer.standard.StandardResultTestAssertions.*;

public class GenericFailureWithStandardResultWriterTest extends IntegrationTestBase {
    protected Context ctx = mock(Context.class);
    protected VersionInformation versionInformation = new VersionInformation("testVersion", "testBuildTimestamp", "testCommitId");
    protected RandomAsciiArtGenerator asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
    protected CodeSnippetGenerator codeSnippetGenerator = mock(CodeSnippetGenerator.class);
    protected ResultWriter writer;

    protected ResultWriter buildWriter() {
        return new StandardResultWriter(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    @BeforeEach
    void setupMocks() throws FileNotFoundException {
        DirectoryConfiguration dirs = new DirectoryConfiguration("any", reportDir.toString());
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

    protected void writeResult(Result result) {
        writer.write(ctx, result);
    }

    @Test
    void genericFailureWithHint() {
        ExecutionStep badStep = mock(ExecutionStep.class);
        doThrow(new org.pitest.help.PitHelpError(org.pitest.help.Help.FAILING_TESTS, 5))
                .when(badStep).execute(any(Context.class), any(ResultBuilder.class));

        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass",
                createSteps(badStep));

        writeResult(result);

        assertThat(result.hasGenericFailure()).isTrue();

        String output = generatedResult();
        assertThat(output)
                .has(not(compilationSuccess()))
                .has(not(testResults()))
                .has(not(genericFailure("")))
                .contains("org.pitest.help.PitHelpError")
                .contains("tests did not pass without mutation")
                .has(flakyTestSuiteMessage());
    }

    @Test
    void genericFailureTest() {
        ExecutionStep badStep = mock(ExecutionStep.class);
        doThrow(new RuntimeException("This is a very bad and scary exception"))
                .when(badStep).execute(any(Context.class), any(ResultBuilder.class));

        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass",
                createSteps(badStep));

        writeResult(result);

        assertThat(result.hasGenericFailure()).isTrue();

        String output = generatedResult();
        assertThat(output)
                .has(not(compilationSuccess()))
                .has(not(testResults()))
                .has(genericFailure("java.lang.RuntimeException: This is a very bad and scary exception"))
                .has(not(flakyTestSuiteMessage()));
    }

    private List<ExecutionStep> createSteps(ExecutionStep badStep) {
        return Arrays.asList(
                new OrganizeSourceCodeStep(),
                new SourceCodeSecurityCheckStep(),
                new CompilationStep(),
                new ReplaceClassloaderStep(),
                new GetRunConfigurationStep(),
                badStep,
                new InjectModeActionStepsStep());
    }
}
