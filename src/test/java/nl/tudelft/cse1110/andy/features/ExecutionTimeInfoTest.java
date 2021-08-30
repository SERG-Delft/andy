package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ResultTestAssertions.totalTimeItTookToExecute;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/* This test class tests that the final time to execute is shown (in the format #.#).
 */
public class ExecutionTimeInfoTest extends IntegrationTestBase {

    @Test
    void checkTotalTimeToExecute() {
        String result = run("ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");
        assertThat(result).has(totalTimeItTookToExecute());
    }

}
