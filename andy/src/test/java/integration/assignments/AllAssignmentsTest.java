package integration.assignments;

import integration.IntegrationTestBase;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("assignments")
public class AllAssignmentsTest extends IntegrationTestBase {

    private static List<String> disabledList = Arrays.asList(
            "property-based-testing/unique",
            "property-based-testing/summer"
            );
    @ParameterizedTest
    @MethodSource("all")
    void runAssignment(String directory) {

        // we set the directory in the property, which gets used inside the
        // selenium code, so that we can point precisely to the folder that
        // contains the HTML
        System.setProperty("assignment.directory", directory);

        String libraryFile = findFirstJavaFile(directory + "/src/main/java/delft");
        String solutionFile = findFirstJavaFile(directory + "/solution");
        String configurationFile = directory + "/config/Configuration.java";

        Result result = run(Action.FULL_WITH_HINTS,
                libraryFile,
                solutionFile,
                configurationFile);

        assertThat(result.hasFailed()).as(format("exercise %s", directory)).isFalse();
        assertThat(result.getFinalGrade()).as(format("exercise %s", directory)).isEqualTo(100);
    }

    private static Stream<Arguments> all() {
        return findDirectoriesWithPom(new File("../assignments"))
                .stream()
                .filter(file -> !isInDisabledList(file))
                .map(file -> Arguments.of(file.getAbsolutePath()));
    }

    private static boolean isInDisabledList(File file) {
        return disabledList.stream()
                .anyMatch(disabled -> file.getAbsolutePath().contains(disabled));
    }

    private static List<File> findDirectoriesWithPom(File rootDirectory) {
        List<File> result = new ArrayList<>();
        findDirectoriesWithPomRecursive(rootDirectory, result);
        return result;
    }

    private static void findDirectoriesWithPomRecursive(File directory, List<File> result) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File pomFile = new File(file, "pom.xml");
                    if (pomFile.exists()) {
                        result.add(file);
                    } else {
                        findDirectoriesWithPomRecursive(file, result);
                    }
                }
            }
        }
    }

    private static String findFirstJavaFile(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    return file.getAbsolutePath();
                }
            }
        }

        throw new RuntimeException("No java file found in " + directoryPath);
    }
}
