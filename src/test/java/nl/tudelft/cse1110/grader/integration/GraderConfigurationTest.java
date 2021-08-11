package nl.tudelft.cse1110.grader.integration;

import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderConfigurationTest extends GraderIntegrationTestBase {

    @Test
    void testSpecifyingClass() {
        String result = runWithConfigNoMeta(withJacoco(), "SoftwhereLibrary", "SoftwhereTests", "SoftwhereConfig");

        assertThat(result).has(linesCovered(13))
                .has(instructionsCovered(58))
                .has(branchesCovered(2));
    }

    @Test
    void testPiTestConfiguration() {
        String result = runWithConfigNoMeta(withPiTest(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddPiTestStrongerConfiguration");

        assertThat(result).has(mutationScore(7, 33));
    }

    @Test
    void testGradeConfiguration() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAdd", "NumberUtilsAddConfiguration");

        assertThat(result).has(finalGrade(46));
    }

}
