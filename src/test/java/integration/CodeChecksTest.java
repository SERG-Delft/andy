package integration;

import org.junit.jupiter.api.Test;
import utils.ResultTestAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeChecksTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecksConfiguration");

        assertThat(result)
                .has(ResultTestAssertions.scoreOfCodeChecks(3,3))
                .has(ResultTestAssertions.codeCheck("Trip Repository should be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("Trip should not be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void someChecksFail() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks2Configuration");

        assertThat(result)
                .has(ResultTestAssertions.scoreOfCodeChecks(2,5))
                .has(ResultTestAssertions.codeCheck("Trip Repository should be mocked", true, 1))
                .has(ResultTestAssertions.codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(ResultTestAssertions.codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void noChecks() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfiguration");
        assertThat(result).doesNotHave(ResultTestAssertions.codeCheckScores());
    }

}
