package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyCodeChecks;
import static org.assertj.core.api.Assertions.assertThat;

public class RunCodeChecksStepTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        String result = run(onlyCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecksConfiguration");

        assertThat(result)
                .has(ResultTestAssertions.scoreOfCodeChecks(3,3))
                .has(ResultTestAssertions.codeCheck("Trip Repository should be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("Trip should not be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void someChecksFail() {
        String result = run(onlyCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks2Configuration");

        assertThat(result)
                .has(ResultTestAssertions.scoreOfCodeChecks(2,5))
                .has(ResultTestAssertions.codeCheck("Trip Repository should be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(ResultTestAssertions.codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void noChecks() {
        String result = run(onlyCodeChecks(), "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfiguration");
        assertThat(result).doesNotHave(ResultTestAssertions.codeCheckScores());
    }

}
