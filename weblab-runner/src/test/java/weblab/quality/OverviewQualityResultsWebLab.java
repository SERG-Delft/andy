package weblab.quality;

import integration.quality.OverviewQualityResults;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testutils.ResultTestDataBuilder;

import java.io.File;
import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.Assertions.assertThat;
import static utils.WebLabEditorFeedbackJsonTestAssertions.editorFeedbackCompilationError;

public class OverviewQualityResultsWebLab extends OverviewQualityResults {

    @Override
    protected ResultWriter buildWriter() {
        return new WebLabResultWriter(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    private String editorFeedbackJson() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "editor-feedback.json")));
    }

    /**
     * For an overview of the results. To be used in production only.
     */
    @ParameterizedTest
    @MethodSource("testSuites")
    void metaTestQualityIsAcceptable(
            String libraryFile,
            String solutionFile,
            String configurationFile
    ) {
        Result result = run(libraryFile, solutionFile, configurationFile);

        writer.write(ctx, result);

        String output = generatedResult();

        System.out.println(output);
    }

    static Stream<Arguments> testSuites() {
        return Stream.of(
                Arguments.of("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration")
        );
    }
}
