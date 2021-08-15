package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Disabled;
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
        File expected = new File(resourceFolder("/grader/fixtures/Output/results_indexof_listcommented_fail.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    // TEO: This one is breaking
    @Test @Disabled
    void resultsXmlWithFullGrade() {
        run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");

        File xmlFile = new File(concatenateDirectories(workDir.toString(), "results.xml"));
        File expected = new File(resourceFolder("/grader/fixtures/Output/results_indexof_listcommented_success.xml"));

        assertThat(xmlFile).exists().isFile();
        assertThat(xmlFile).hasSameTextualContentAs(expected);
    }

    // TEO: Add one more here, for a grade that's not 100/100
    @Test @Disabled
    void resultsXmlWithPartialGrade() {

    }
}
