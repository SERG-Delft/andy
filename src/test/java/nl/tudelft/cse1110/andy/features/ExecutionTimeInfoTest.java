package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyJUnitTests;
import static nl.tudelft.cse1110.andy.ResultTestAssertions.totalTimeItTookToExecute;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/* This test class tests that the final time to execute is shown (in the format #.#).
 */
public class ExecutionTimeInfoTest extends IntegrationTestBase {

    @ParameterizedTest
    @CsvSource({
        "ZagZigLibrary,ZagZigNotAllMutantsKilled,ZagZigDifferentTotalMutantsConfiguration",
        "NumberUtilsAddLibrary,NumberUtilsAddAllTestsPass,NumberUtilsAddConfiguration"
    })
    void checkTotalTimeToExecute(String library, String solution, String config) {
        String result = run(onlyJUnitTests(), library, solution, config);
        assertThat(result).has(totalTimeItTookToExecute());
    }

}
