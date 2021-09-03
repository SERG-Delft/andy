package unit.writer.weblab;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.CoverageLineByLine;
import nl.tudelft.cse1110.andy.result.CoverageResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import testutils.ResultTestDataBuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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

    // TODO: tests for grade 0 if compilation fails. Was deleted from Compilation.

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

}
