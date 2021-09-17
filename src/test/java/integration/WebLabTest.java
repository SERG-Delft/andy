package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WebLabTest extends IntegrationTestBase {

    protected ResultWriter getWriter() {
        RandomAsciiArtGenerator generator = new RandomAsciiArtGenerator();
        return new WebLabResultWriter(ctx, generator);
    }

    @Test
    void allFilesAreGenerated() {

        run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        assertThat(reportDir)
                .isDirectoryContaining(f -> f.getName().equals("stdout.txt"))
                .isDirectoryContaining(f -> f.getName().equals("results.xml"))
                .isDirectoryContaining(f -> f.getName().equals("highlights.json"))
                .isDirectoryContaining(f -> f.getName().equals("post.json"));
    }
}
