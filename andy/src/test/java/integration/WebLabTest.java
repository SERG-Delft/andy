package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WebLabTest extends IntegrationTestBase {

    protected ResultWriter getWriter() {
        RandomAsciiArtGenerator randomAsciiArtGenerator = new RandomAsciiArtGenerator();
        CodeSnippetGenerator codeSnippetGenerator = new CodeSnippetGenerator();
        VersionInformation versionInformation = new VersionInformation("testVersion", "testBuildTimestamp", "testCommitId");
        return new WebLabResultWriter(versionInformation, randomAsciiArtGenerator, codeSnippetGenerator);
    }

    @Test
    void allFilesAreGenerated() {

        run(Action.FULL_WITH_HINTS, "ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(reportDir)
                .isDirectoryContaining(f -> f.getName().equals("stdout.txt"))
                .isDirectoryContaining(f -> f.getName().equals("results.xml"))
                .isDirectoryContaining(f -> f.getName().equals("editor-feedback.json"))
                .isDirectoryContaining(f -> f.getName().equals("post.json"));

        String stdout = readFile(new File(reportDir.getAbsolutePath() + "/stdout.txt"));
        assertThat(stdout)
                .contains("6/6 passed")
                .contains("Final grade: 100/100");
    }
}
