package integration;

import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import org.junit.jupiter.api.Test;

import static utils.ResultTestAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GradeReportTest extends IntegrationTestBase {

    private String asciiArtExpected = "     Super congrats!\n" +
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

    protected RandomAsciiArtGenerator getAsciiArtGenerator() {
        RandomAsciiArtGenerator asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
        when(asciiArtGenerator.getRandomAsciiArt()).thenReturn(asciiArtExpected);

        return asciiArtGenerator;
    }

    @Test
    void printAsciiArtWhen100Score() {

        String result = run(
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsConfiguration");

        assertThat(result).has(asciiArtPrinted(asciiArtExpected));
        assertThat(result).has(totalTimeItTookToExecute());
    }

    @Test
    void showFullDescriptionOfTheGrade() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");
        System.out.println(result);

        assertThat(result)
                .has(finalGrade(workDir.toString(), 42))
                .has(fullGradeDescription("Branch coverage", 13, 22, 0.10))
                .has(fullGradeDescription("Mutation coverage", 6, 30, 0.30))
                .has(fullGradeDescription("Code checks", 0, 0, 0.20))
                .has(fullGradeDescription("Meta tests", 1, 4, 0.40));

        assertThat(result).has(totalTimeItTookToExecute());
    }


}
