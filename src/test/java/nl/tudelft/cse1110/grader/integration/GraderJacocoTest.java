package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderJacocoTest extends GraderIntegrationTestBase {

    @ParameterizedTest
    @MethodSource("generator")
    void test(String library, String solution, int lines, int instructions, int branches) {
        String result = run(withJacoco(), library, solution);

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
                Arguments.of("SoftwhereLibrary", "SoftwhereTests", 45, 183, 10),

                //test multiple classes in the solution
                Arguments.of("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", 8, 25, 8)
        );
    }
}
