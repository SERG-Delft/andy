package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ExecutionStepHelper.justCodeChecks;
import static nl.tudelft.cse1110.ResultTestAssertions.codeCheck;
import static nl.tudelft.cse1110.ResultTestAssertions.scoreOfCodeChecks;
import static org.assertj.core.api.Assertions.assertThat;

public class RunCodeChecksStepTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        String result = run(justCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks");

        assertThat(result)
                .has(scoreOfCodeChecks(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

}
