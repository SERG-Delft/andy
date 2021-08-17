package nl.tudelft.cse1110.acceptance;

import nl.tudelft.cse1110.ExecutionStepHelper;
import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.ResourceUtils.resourceFolder;
import static nl.tudelft.cse1110.grader.util.FileUtils.concatenateDirectories;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResultXmlTest extends IntegrationTestBase {

    @Test
    void resultsXmlWithGrade0() {
        run(ExecutionStepHelper.fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsIndexOfListCommentedFail.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithFullGrade() {
        run(ExecutionStepHelper.fullMode(), "MathArraysLibrary", "MathArrays100Score");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsMathArraysSuccess.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithPartialGrade() {
        run(ExecutionStepHelper.fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsNumberUtilsPartial.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }
}
