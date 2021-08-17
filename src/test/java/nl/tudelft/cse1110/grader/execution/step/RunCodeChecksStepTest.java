package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ExecutionStepHelper.onlyCodeChecks;
import static nl.tudelft.cse1110.ResultTestAssertions.codeCheck;
import static nl.tudelft.cse1110.ResultTestAssertions.scoreOfCodeChecks;
import static org.assertj.core.api.Assertions.assertThat;

public class RunCodeChecksStepTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        String result = run(onlyCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks");

        assertThat(result)
                .has(scoreOfCodeChecks(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void someChecksFail() {
        String result = run(onlyCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks2");

        assertThat(result)
                .has(scoreOfCodeChecks(2,5))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(codeCheck("getTripById should be set up", true, 1));
    }

}
