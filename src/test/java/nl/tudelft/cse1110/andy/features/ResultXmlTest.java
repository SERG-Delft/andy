package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.ExecutionStepHelper;
import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResourceUtils;
import nl.tudelft.cse1110.andy.grader.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResultXmlTest extends IntegrationTestBase {

    @Test
    void resultsXmlWithGrade0() {
        run(ExecutionStepHelper.fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File xmlFile = new File(FileUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsIndexOfListCommentedFail.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithFullGrade() {
        run(ExecutionStepHelper.fullMode(), "MathArraysLibrary", "MathArrays100Score");

        File xmlFile = new File(FileUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsMathArraysSuccess.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithPartialGrade() {
        run(ExecutionStepHelper.fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        File xmlFile = new File(FileUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsNumberUtilsPartial.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }
}
