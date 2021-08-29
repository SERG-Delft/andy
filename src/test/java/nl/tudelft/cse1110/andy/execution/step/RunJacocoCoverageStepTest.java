package nl.tudelft.cse1110.andy.execution.step;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.result.Highlight;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyBranchCoverage;
import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunJacocoCoverageStepTest extends IntegrationTestBase {

    @ParameterizedTest
    @MethodSource("generator")
    void test(String library, String solution, int lines, int instructions, int branches) {
        String result = run(onlyBranchCoverage(), library, solution);

        assertThat(result)
                .has(linesCovered(lines))
                .has(instructionsCovered(instructions))
                .has(branchesCovered(branches));
    }

    static Stream<Arguments> generator() {
        return Stream.of(
                //test when all branches are covered
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddSmoke", 20, 112, 22),

                //test when only some branches are covered
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", 15, 87, 13),

                //test when there are no tests
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsNoTests", 0, 0, 0),

                //test multiple classes in the library
                Arguments.of("SoftWhereLibrary", "SoftWhereTests", 45, 183, 10),

                //test multiple classes in the solution
                Arguments.of("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", 8, 25, 8)
        );
    }

    @ParameterizedTest
    @MethodSource("highlightsGenerator")
    void highlights(String library, String solution, int[] coveredLines, int[] partiallyCovered, int[] notCovered) throws FileNotFoundException {
        String result = run(onlyBranchCoverage(), library, solution);

        File highlights = new File(FilesUtils.concatenateDirectories(workDir.toString(), "highlights.json"));
        Type listType = new TypeToken<ArrayList<Highlight>>(){}.getType();

        List<Highlight> list = new Gson().fromJson(new FileReader(highlights), listType);

        for (int line : coveredLines) {
            boolean lineCovered = list.stream().anyMatch(c -> c.getLine() == line && c.getLocation() == Highlight.HighlightLocation.LIBRARY && c.getPurpose() == Highlight.HighlightPurpose.FULL_COVERAGE);
            assertThat(lineCovered).as("fully covered line %d", line).isTrue();
        }

        for (int line : partiallyCovered) {
            boolean lineCovered = list.stream().anyMatch(c -> c.getLine() == line && c.getLocation() == Highlight.HighlightLocation.LIBRARY && c.getPurpose() == Highlight.HighlightPurpose.PARTIAL_COVERAGE);
            assertThat(lineCovered).as("partially covered line %d in json", line).isTrue();

            assertThat(result).as("partially covered line %d in result", line)
                    .has(partiallyCoveredLine(line));
        }

        for (int line : notCovered) {
            boolean lineCovered = list.stream().anyMatch(c -> c.getLine() == line && c.getLocation() == Highlight.HighlightLocation.LIBRARY && c.getPurpose() == Highlight.HighlightPurpose.NO_COVERAGE);
            assertThat(lineCovered).as("not covered line %d in json", line).isTrue();

            assertThat(result).as("not covered line %d in result", line)
                    .has(notCoveredLine(line));
        }
    }

    static Stream<Arguments> highlightsGenerator() {
        return Stream.of(
            Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddSmoke",
                    new int[] {34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 61, 62, 63, 64, 65, 66, 67, 68},
                    new int[] {},
                    new int[] {}),
            Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass",
                    new int[] {36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 61, 63, 64, 67},
                    new int[] {34, 48, 49, 50},
                    new int[] {35, 62, 65, 66, 68})
        );
    }

    // not really needed, as this was meant to exercise the configuration, but let's keep it for now
    @Test
    void specifyingConfigClass() {
        String result = run(onlyBranchCoverage(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfiguration");

        assertThat(result).has(linesCovered(13))
                .has(instructionsCovered(58))
                .has(branchesCovered(2));
    }

}
