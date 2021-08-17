package nl.tudelft.cse1110.acceptance;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ExecutionStepHelper.fullMode;
import static nl.tudelft.cse1110.ResultTestAssertions.finalGrade;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FinalGradeCalculationTest extends IntegrationTestBase {

    @Test
    void calculatesCorrectFinalGrade() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");
        assertThat(result).has(finalGrade(46));
    }

}
