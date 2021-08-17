package nl.tudelft.cse1110.grader.execution.step;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.execution.step.GraderIntegrationTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderMutationsTest extends GraderIntegrationTestBase {


    @Test
    void testPiTestConfiguration() {
        String result = run(GraderIntegrationTestHelper.withPiTest(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(7, 33));
    }


    //    @Test
//    void testAllMutationsKilled() {
//
//        // TODO: we should be able to configure/override the total number of mutants for this (equivalent mutants...)
//
//        String result = run(withPiTest(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsSubtractPiTestOldDefaults");
//
//        System.out.println(result);
//
//        assertThat(result)
//                .has(mutationScore(100, 100));
//    }

}
