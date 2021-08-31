package unit.writer.weblab;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import testutils.ResultTestDataBuilder;

import java.io.File;
import java.nio.file.Path;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static testutils.ResultTestAssertions.*;

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
    }

    private String generatedResult() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "stdout.txt")));
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
                .has(compilationErrorType("some compilation error"))
                .has(compilationErrorType("some other compilation error"));
    }

}
