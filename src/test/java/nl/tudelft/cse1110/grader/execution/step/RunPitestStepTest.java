package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.ExecutionStepHelper;
import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunPitestStepTest extends IntegrationTestBase {


    @Test
    void mutantsSurvived() {
        String result = run(ExecutionStepHelper.onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(7, 33));
    }

    // TODO: all mutants killed

    // TODO: different total mutants configuration

}
