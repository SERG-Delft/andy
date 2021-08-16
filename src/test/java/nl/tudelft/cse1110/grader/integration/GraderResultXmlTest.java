package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.ResourceUtils.resourceFolder;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.fullMode;
import static nl.tudelft.cse1110.grader.util.FileUtils.concatenateDirectories;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderResultXmlTest extends GraderIntegrationTestBase{

    @Test
    void resultsXmlWithGrade0() {
        run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsIndexOfListCommentedFail.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithFullGrade() {
        run(fullMode(), "MathArraysLibrary", "MathArrays100Score");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsMathArraysSuccess.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    @Test
    void resultsXmlWithPartialGrade() {
        run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/resultsNumberUtilsPartial.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }
}
