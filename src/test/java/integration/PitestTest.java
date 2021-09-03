package integration;

import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PitestTest extends IntegrationTestBase {


    /* Test where all mutants are killed.
     * 32 killed mutants means 100%, because 1 of the 33 identified mutants cannot be killed.
     * TODO: replace this test by one that kills all the mutants!!
     */
    @Test
    void allMutantsButOneKilled() {
        Result result = run2("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(32);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(33);
    }


    /* Test where some of the mutants are killed.
     */
    @Test
    void mutantsSurvived() {
        Result result = run2("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(7);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(33);

    }

    /* Test where a different total number of mutants is specified.
     * All are killed by the solution.
     */
    @Test
    void overriddenNumberOfTotalMutantsAllKilled() {
        Result result = run2("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(26);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(26);

    }


    /* Test where a different total number of mutants is specified.
     * Only some of them are killed by the solution.
     */
    @Test
    void overriddenNumberOfTotalMutantsNotAllKilled() {
        Result result = run2("ZagZigLibrary", "ZagZigNotAllMutantsKilled", "ZagZigDifferentTotalMutantsConfiguration");

        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(24);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(26);

    }


    /*
     * Solution kills more mutants than the overridden number of mutants.
     */
    @Test
    void solutionKillsMoreThanOverriddenNumberOfMutants() {
        // overrides number of mutants by 10, but solution kills 26
        Result result = run2("ZagZigLibrary", "ZagZigAllMutantsKilled", "ZagZigDifferentTotalMutantsConfigurationLessThanStudent");

        assertThat(result.getMutationTesting().getKilledMutants()).isEqualTo(10);
        assertThat(result.getMutationTesting().getTotalNumberOfMutants()).isEqualTo(10);
    }

}
