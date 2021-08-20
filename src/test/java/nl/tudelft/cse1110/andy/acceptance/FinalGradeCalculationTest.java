package nl.tudelft.cse1110.andy.acceptance;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FinalGradeCalculationTest extends IntegrationTestBase {

    @Test
    void calculatesCorrectFinalGrade() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");
        assertThat(result).has(ResultTestAssertions.finalGrade(46));
    }

}
