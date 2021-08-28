package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.grader.execution.mode.Action;
import nl.tudelft.cse1110.andy.grader.result.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyBasic;
import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FinalGradePrintAsciiArtTest extends IntegrationTestBase {

    private String asciiArtExpected;
    private RandomAsciiArtGenerator asciiArtGenerator;
    private ResultBuilder resultBuilder;

    @BeforeEach
    void setup() {

        asciiArtGenerator = mock(RandomAsciiArtGenerator.class);

        asciiArtExpected =  "     Super congrats!\n" +
                "\n" +
                "            __,__\n" +
                "   .--.  .-\"     \"-.  .--.\n" +
                "  / .. \\/  .-. .-.  \\/ .. \\\n" +
                " | |  '|  /   Y   \\  |'  | |\n" +
                " | \\   \\  \\ 0 | 0 /  /   / |\n" +
                "  \\ '- ,\\.-\"`` ``\"-./, -' /\n" +
                "   `'-' /_   ^ ^   _\\ '-'`\n" +
                "       |  \\._   _./  |\n" +
                "       \\   \\ `~` /   /\n" +
                "        '._ '-=-' _.'\n" +
                "           '~---~'\n";
        when(asciiArtGenerator.getRandomAsciiArt()).thenReturn(asciiArtExpected);

        resultBuilder = new ResultBuilder(asciiArtGenerator);
    }


    // 0.1 * 22/22 + 0.3 * 29/29 + 0.4 * 4/4 + 0.2 --> 100
    @Test
    void printAsciiArtWhen100Score() {

        String result = run(fullMode(),
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsConfiguration", resultBuilder);

        assertThat(result).has(asciiArtPrinted(asciiArtExpected));
    }


    // In exam mode, no matter what action, the final grade should not be printed
    @ParameterizedTest
    @MethodSource("testExamActionGenerator")
    void noAsciiArtInExamMode(Action action) {

        String result = run(action, onlyBasic(),
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsExamModeConfiguration", resultBuilder);

        assertThat(result)
                .has(noFinalGrade())
                .doesNotHave(asciiArtPrinted(asciiArtExpected));
    }

    static Stream<Arguments> testExamActionGenerator() {
        return Stream.of(
                Arguments.of(Action.FULL_WITH_HINTS),
                Arguments.of(Action.FULL_WITHOUT_HINTS),
                Arguments.of(Action.COVERAGE)
        );
    }


    // In practice mode, grade should be shown for both actions HINTS and NO_HINTS

    @ParameterizedTest
    @MethodSource("testPracticeActionGenerator")
    void asciiArtInPracticeFullMode(Action action) {

        String result = run(action, onlyBasic(),
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsPracticeModeConfiguration", resultBuilder);

        assertThat(result)
                .has(finalGrade(100))
                .has(asciiArtPrinted(asciiArtExpected));
    }

    static Stream<Arguments> testPracticeActionGenerator() {
        return Stream.of(
                Arguments.of(Action.FULL_WITH_HINTS),
                Arguments.of(Action.FULL_WITHOUT_HINTS)
        );
    }




    // This unit test stubs the calculated final grade
    @Test
    void logFinalGradePrintAsciiUnitTest() {

        resultBuilder.message("line 1");
        resultBuilder.message("line 2");
        resultBuilder.printRandomAsciiArt();

        String result = resultBuilder.buildEndUserResult();

        assertThat(result)
                .startsWith("line 1\n" +
                "line 2\n")
                .endsWith(
                asciiArtExpected + "\n");

    }


}
