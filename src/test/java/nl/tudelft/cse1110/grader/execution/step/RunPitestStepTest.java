package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.ExecutionStepHelper;
import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunPitestStepTest extends IntegrationTestBase {


    // Test where some of the mutants are killed.
    @Test
    void mutantsSurvived() {
        String result = run(ExecutionStepHelper.onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(7, 33));
    }

    // TODO: all mutants killed

    // TODO: different total mutants configuration

    // Test where a different total number of mutants is specified.
    // All are killed by the solution.
    @Test
    void differentNumberOfTotalMutantsAllKilled() {
        String result = run(ExecutionStepHelper.onlyMutationCoverage(), "ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfig");

        assertThat(result).has(mutationScore(26, 26));
    }

    // Test where a different total number of mutants is specified.
    // Only some of them are killed by the solution.
    @Test
    void differentNumberOfTotalMutantsNotAllKilled() {
        String result = run(ExecutionStepHelper.onlyMutationCoverage(), "ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfig");

        assertThat(result).has(mutationScore(24, 26));
    }

}
