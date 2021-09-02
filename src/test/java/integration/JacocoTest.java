package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JacocoTest extends IntegrationTestBase {

    @ParameterizedTest
    @MethodSource("generator")
    void differentCoverages(String library, String solution, int lines, int instructions, int branches) {
        Result result = run2(Action.COVERAGE, library, solution);

        assertThat(result.getCoverage().wasExecuted()).isTrue();

        assertThat(result.getCoverage().getTotalCoveredLines()).isEqualTo(lines);
        assertThat(result.getCoverage().getTotalCoveredInstructions()).isEqualTo(instructions);
        assertThat(result.getCoverage().getTotalCoveredBranches()).isEqualTo(branches);
    }

    static Stream<Arguments> generator() {
        return Stream.of(
                //test when all branches are covered
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddSmoke", 20, 112, 22),

                //test when only some branches are covered
                Arguments.of("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", 15, 87, 13),

                //test multiple classes in the library
                Arguments.of("SoftWhereLibrary", "SoftWhereTests", 45, 183, 10),

                //test multiple classes in the solution
                Arguments.of("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", 8, 25, 8)
        );
    }

    @Test
    void doesNotHaveTests() {
        Result result = run2(Action.COVERAGE, "NumberUtilsAddLibrary", "NumberUtilsNoTests");

        assertThat(result.getCoverage().wasExecuted()).isTrue();

        assertThat(result.getCoverage().getTotalCoveredLines()).isEqualTo(0);
        assertThat(result.getCoverage().getTotalCoveredInstructions()).isEqualTo(0);
        assertThat(result.getCoverage().getTotalCoveredBranches()).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("coveredLinesGenerator")
    void coveredAndUncoveredLines(String library, String solution, int[] coveredLines, int[] partiallyCovered, int[] notCovered) throws FileNotFoundException {
        Result result = run2(Action.COVERAGE, library, solution);

        assertThat(result.getCoverage().getFullyCoveredLines()).isEqualTo(coveredLines);
        assertThat(result.getCoverage().getPartiallyCoveredLines()).isEqualTo(partiallyCovered);
        assertThat(result.getCoverage().getNotCoveredLines()).isEqualTo(notCovered);
    }

    static Stream<Arguments> coveredLinesGenerator() {
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

}
