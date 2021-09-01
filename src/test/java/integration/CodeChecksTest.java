package integration;

import org.junit.jupiter.api.Test;
import testutils.WebLabTestAssertions;

import static org.assertj.core.api.Assertions.assertThat;
import static testutils.WebLabTestAssertions.codeCheck;
import static testutils.WebLabTestAssertions.codeCheckScores;

public class CodeChecksTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecksConfiguration");

        assertThat(result)
                .has(WebLabTestAssertions.scoreOfCodeChecks(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void someChecksFail() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks2Configuration");

        assertThat(result)
                .has(WebLabTestAssertions.scoreOfCodeChecks(2,5))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void noChecks() {
        String result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfiguration");
        assertThat(result).doesNotHave(codeCheckScores());
    }

}
