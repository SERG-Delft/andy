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
        String result = run(withJacoco(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfig");

        assertThat(result).has(linesCovered(13))
                .has(instructionsCovered(58))
                .has(branchesCovered(2));
    }


    @Test
    void testGradeConfiguration() {
        String result = run(fullMode(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result).has(finalGrade(46));
    }

// TODO: synchronize with Teo's PR

//    @Test
//    void testFailureGivesZero() {
//
//        String result = run(justFinalGrade(), "LeapYearLibrary", "LeapYearSingleTestFails", "LeapYearFailureGivesZeroConfig");
//
//        assertThat(result).has(finalGrade(0));
//    }


}
