package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyMutationCoverage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunPitestStepTest extends IntegrationTestBase {


    /* Test where some of the mutants are killed.
     */
    @Test
    void mutantsSurvived() {
        String result = run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(7, 33));
    }


    /* Test where all mutants are killed.
     * 32 killed mutants means 100%, because 1 of the 33 identified mutants cannot be killed.
     */
    @Test
    void allMutantsKilled() {
        String result = run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(32, 33));
    }


    /* Test where a different total number of mutants is specified.
     * All are killed by the solution.
     */
    @Test
    void differentNumberOfTotalMutantsAllKilled() {
        String result = run(onlyMutationCoverage(), "ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(26, 26));
    }


    /* Test where a different total number of mutants is specified.
     * Only some of them are killed by the solution.
     */
    @Test
    void differentNumberOfTotalMutantsNotAllKilled() {
        String result = run(onlyMutationCoverage(), "ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(24, 26));
    }


    /* Test where not all of the identified mutants are killed and a report is generated.
     * The default run configuration is used.
     * The test checks whether the report directory is generated and the log shows "See the attached report."
     */
    @Test
    void reportWasGenerated() {
        run(onlyMutationCoverage(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");
        File report = new File(reportDir.toString() + "/pitest");

        assertThat(report).exists().isDirectory();
    }

}
