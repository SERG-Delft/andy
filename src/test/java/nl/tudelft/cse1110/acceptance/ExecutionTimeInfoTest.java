package nl.tudelft.cse1110.acceptance;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ExecutionStepHelper.fullMode;
import static nl.tudelft.cse1110.ResultTestAssertions.totalTimeToExecute;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// This test class tests that the final time to execute is shown (in the format #.#).
public class ExecutionTimeInfoTest extends IntegrationTestBase {

    @Test
    void checkTotalTimeToExecute() {
        String result = run(fullMode(), "ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfig");
        assertThat(result).has(totalTimeToExecute());
    }

    @Test
    void checkTotalTimeToExecute2() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");
        assertThat(result).has(totalTimeToExecute());
    }

}
