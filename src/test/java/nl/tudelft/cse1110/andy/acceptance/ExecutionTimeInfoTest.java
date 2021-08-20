package nl.tudelft.cse1110.andy.acceptance;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.fullMode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/* This test class tests that the final time to execute is shown (in the format #.#).
 */
public class ExecutionTimeInfoTest extends IntegrationTestBase {

    @Test
    void checkTotalTimeToExecute() {
        String result = run(fullMode(), "ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");
        assertThat(result).has(ResultTestAssertions.totalTimeToExecute());
    }

    @Test
    void checkTotalTimeToExecute2() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");
        assertThat(result).has(ResultTestAssertions.totalTimeToExecute());
    }

}
