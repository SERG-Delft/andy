package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.ExecutionStepHelper.onlyMutationCoverage;
import static nl.tudelft.cse1110.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunPitestStepTest extends IntegrationTestBase {


    // Test where some of the mutants are killed.
    @Test
    void mutantsSurvived() {
        String result = run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(7, 33));
    }


    // Test where not all of the identified mutants are killed and a report is generated. The default run configuration is used.
    // The test checks whether the report directory is generated and the log shows "See attached report."
    @Test
    void reportWasGenerated() {
        String result = run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");
        File report = new File(reportDir.toString() + "/pitest");

        assertThat(report).exists().isDirectory();
        assertThat(result).has(pitestReport());
    }


    // Test where all mutants are killed.
    // 32 killed mutants means 100%, because 1 of the 33 identified mutants cannot be killed.
    @Test
    void allMutantsKilled() {
        String result = run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(32, 33));
    }


    // Test where a different total number of mutants is specified.
    // All are killed by the solution.
    @Test
    void differentNumberOfTotalMutantsAllKilled() {
        String result = run(onlyMutationCoverage(), "ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfig");

        assertThat(result).has(mutationScore(26, 26));
    }


    // Test where a different total number of mutants is specified.
    // Only some of them are killed by the solution.
    @Test
    void differentNumberOfTotalMutantsNotAllKilled() {
        String result = run(onlyMutationCoverage(), "ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfig");

        assertThat(result).has(mutationScore(24, 26));
    }

}
