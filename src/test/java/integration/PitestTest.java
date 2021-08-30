package integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import utils.ResultTestAssertions;

import java.io.File;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PitestTest extends IntegrationTestBase {


    /* Test where some of the mutants are killed.
     */
    @Test
    void mutantsSurvived() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(7, 33));
    }


    /* Test where all mutants are killed.
     * 32 killed mutants means 100%, because 1 of the 33 identified mutants cannot be killed.
     */
    @Test
    void allMutantsKilled() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(32, 33));
    }

    @Test
    void solutionKillsMoreThanOverriddenNumberOfMutants() {
        // overrides number of mutants by 10, but solution kills 26
        String result = run("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfigurationLessThanStudent");

        assertThat(result).has(ResultTestAssertions.mutationScore(10, 10));
    }

    /* Test where a different total number of mutants is specified.
     * All are killed by the solution.
     */
    @Test
    void differentNumberOfTotalMutantsAllKilled() {
        String result = run("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(26, 26));
    }


    /* Test where a different total number of mutants is specified.
     * Only some of them are killed by the solution.
     */
    @Test
    void differentNumberOfTotalMutantsNotAllKilled() {
        String result = run("ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result).has(ResultTestAssertions.mutationScore(24, 26));
    }


    /* Test where not all of the identified mutants are killed and a report is generated.
     * The default run configuration is used.
     * The test checks whether the report directory is generated and the log shows "See the attached report."
     */
    @AfterEach
    void reportWasGenerated() {
        File report = new File(reportDir.toString() + "/pitest");
        assertThat(report).exists().isDirectory();
    }

}
