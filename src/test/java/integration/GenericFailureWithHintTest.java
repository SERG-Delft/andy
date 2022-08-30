package integration;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static unit.writer.standard.StandardResultTestAssertions.*;

public class GenericFailureWithHintTest extends IntegrationTestBase {

    @Override
    protected void addSteps(ExecutionFlow flow) {
        ExecutionStep badStep = mock(ExecutionStep.class);

        doThrow(new org.pitest.help.PitHelpError(org.pitest.help.Help.FAILING_TESTS, 5))
                .when(badStep).execute(any(Context.class), any(ResultBuilder.class));

        flow.addSteps(List.of(badStep));
    }

    @Test
    void genericFailureWithHint() {
        // Arrange
        Context ctx = mock(Context.class);
        DirectoryConfiguration dirs = new DirectoryConfiguration("any", reportDir.toString());
        when(ctx.getDirectoryConfiguration()).thenReturn(dirs);
        StandardResultWriter writer = new StandardResultWriter(
                new VersionInformation("testVersion", "testBuildTimestamp", "testCommitId"),
                mock(RandomAsciiArtGenerator.class));

        // Act
        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        writer.write(ctx, result);

        // Assert
        assertThat(result.hasGenericFailure()).isTrue();

        String output = readFile(new File(concatenateDirectories(reportDir.toString(), "stdout.txt")));

        assertThat(output)
                .has(not(compilationSuccess()))
                .has(not(testResults()))
                .has(not(genericFailure("")))
                .contains("org.pitest.help.PitHelpError")
                .contains("tests did not pass without mutation")
                .contains("It appears that your test suite is flaky.");
    }
}
