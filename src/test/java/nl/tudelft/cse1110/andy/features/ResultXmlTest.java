package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResourceUtils;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyJUnitTests;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ResultXmlTest extends IntegrationTestBase {

    @Test
    void resultsXmlWithGrade0() {
        run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File xmlFile = new File(FilesUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsIndexOfListCommentedFail.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithFullGrade() {
        run(onlyJUnitTests(), "MathArraysLibrary", "MathArrays100Score");

        File xmlFile = new File(FilesUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsMathArraysSuccess.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithPartialGrade() {
        run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        File xmlFile = new File(FilesUtils.concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(ResourceUtils.resourceFolder("/grader/fixtures/Output/resultsNumberUtilsPartial.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }
}
