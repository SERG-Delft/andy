package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import nl.tudelft.cse1110.andy.TestResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FinalGradeCalculationTest extends IntegrationTestBase {

    // 0.1 * 13/22 + 0.3 * 6/30 + 0.4 * 1/4 + 0.2 --> 42
    @Test
    void calculatesCorrectFinalGrade() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result).has(ResultTestAssertions.finalGrade(42));
    }



    // 0.1 * 22/22 + 0.3 * 29/29 + 0.4 * 4/4 + 0.2 --> 100
    @Test
    void printAsciiArtWhen100Score() {

        String result = run(fullMode(),
                "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution",
                "NumberUtilsAddFullPointsConfiguration");

        assertThat(result).has(ResultTestAssertions.finalGrade(100));
    }

}
