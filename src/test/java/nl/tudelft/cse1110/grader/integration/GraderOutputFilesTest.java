package nl.tudelft.cse1110.grader.integration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.*;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.fullMode;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justCompilation;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderOutputFilesTest extends GraderIntegrationTestBase{

    //!!! import org.apache.commons.io.FileUtils;
    //!!! NOT OUR FileUtils
    private boolean isEqual(File firstFile, File secondFile) {
        try {
            return FileUtils.contentEquals(firstFile, secondFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File fileWithParentDirCreated(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return new File(directory + "/" + filename);
    }

    @Test
    void compilationFailureStdoutFile(){
        String result = run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented", "ArrayUtilsIndexOfJQWikConfig");
        File stdout = fileWithParentDirCreated(System.getenv("OUTPUT_DIR"), "stdout.txt");
        System.out.println(stdout.getAbsolutePath());

        File expected = new File("src/test/resources/grader/fixtures/Output/stdout_indexof_listcommented_fail.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(stdout))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertThat(stdout.exists() && stdout.isFile()).isTrue();
        assertThat(isEqual(stdout,expected)).isTrue();
    }

    @Test
    void compilationSuccessStdoutFile() {
        String result = run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");
        File stdout = new File(System.getenv("OUTPUT_DIR") + "stdout.txt");
        File expected = new File("src/test/resources/grader/fixtures/Output/stdout_indexof_listcommented_success.txt");

        assertThat(stdout.exists() && stdout.isFile()).isTrue();
        assertThat(isEqual(stdout,expected)).isTrue();
    }

    @Test
    void compilationFailureXMLFile() {
        String result = run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");
        File xmlfile = new File(System.getenv("OUTPUT_DIR") + "results.xml");
        File expected = new File("src/test/resources/grader/fixtures/Output/results_indexof_listcommented_fail.xml");

        assertThat(xmlfile.exists() && xmlfile.isFile()).isTrue();
        assertThat(isEqual(xmlfile,expected)).isTrue();
    }

    @Test
    void compilationSuccessXMLFile() {
        String result = run(fullMode(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfImportListCommented");
        File stdout = new File(System.getenv("OUTPUT_DIR") + "results.xml");
        File expected = new File("src/test/resources/grader/fixtures/Output/stdout_indexof_listcommented_success.txt");

        assertThat(stdout.exists() && stdout.isFile()).isTrue();
        assertThat(isEqual(stdout,expected)).isTrue();
    }
}
