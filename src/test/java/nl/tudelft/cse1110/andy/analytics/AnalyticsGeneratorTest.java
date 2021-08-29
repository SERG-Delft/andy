package nl.tudelft.cse1110.andy.analytics;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyBasic;
import static org.assertj.core.api.Assertions.assertThat;

public class AnalyticsGeneratorTest extends IntegrationTestBase {

    @Test
    void generateFileToBePosted() {
        run(Action.FULL_WITH_HINTS, onlyBasic(), "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        File file = new File(FilesUtils.concatenateDirectories(workDir.toString(), "post.json"));
        String json = FilesUtils.readFile(file);

        assertThat(file).exists();

        Submission submission = new Gson().fromJson(json, Submission.class);
        assertThat(submission.getFinalGrade()).isEqualTo(91);
        // TODO: some more asserts here to assert the content
    }

}
