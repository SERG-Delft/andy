package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import nl.tudelft.cse1110.andy.TestResourceUtils;
import nl.tudelft.cse1110.andy.grader.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.grader.grade.GradeValues;
import nl.tudelft.cse1110.andy.grader.grade.GradeWeight;
import nl.tudelft.cse1110.andy.grader.result.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class PrintAsciiArtTest extends IntegrationTestBase {

    RandomAsciiArtGenerator asciiArtGenerator;
    GradeCalculator gradeCalculator;
    ResultBuilder resultBuilder;

    @BeforeEach
    void setup() {
        asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
        gradeCalculator = mock(GradeCalculator.class);

        when(asciiArtGenerator.pickRandomAsciiArt())
                .thenReturn(new File(TestResourceUtils.resourceFolder("grader/fixtures/Congrats/monkey.txt")));

        resultBuilder = new ResultBuilder(asciiArtGenerator, gradeCalculator);
    }


    // 0.1 * 22/22 + 0.3 * 29/29 + 0.4 * 4/4 + 0.2 --> 100
    @Test
    void printAsciiArtWhen100Score() {

        String result = run(fullMode(),
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsConfiguration", resultBuilder);

        assertThat(result).has(ResultTestAssertions
                .asciiArtPrinted(new File(TestResourceUtils.resourceFolder("grader/fixtures/Congrats/monkey.txt"))));
    }


    // This unit test stubs the calculated final grade
    @Test
    void logFinalGradePrintAsciiUnitTest() {

        when(gradeCalculator.calculateFinalGrade(any(GradeValues.class)))
                .thenReturn(100);

        resultBuilder.logFinalGrade();
        String result = resultBuilder.buildEndUserResult();

        assertThat(result).contains(
                "--- Final grade\n" +
                "100/100\n" +
                "\n" +
                "     Super congrats!\n" +
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
                "           '~---~'");

    }




}
