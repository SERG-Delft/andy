package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.withMeta;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.withPiTest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderMutationsTest extends GraderIntegrationTestBase {


    @Test
    void testAllMutationsKilled() {

        // TODO: we should be able to configure/override the total number of mutants for this (equivalent mutants...)

        String result = run(withPiTest(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsSubtractPiTestOldDefaults");

//        System.out.println(result);

//        assertThat(result)
//                .has(mutationScore(100, 100));
    }

}
