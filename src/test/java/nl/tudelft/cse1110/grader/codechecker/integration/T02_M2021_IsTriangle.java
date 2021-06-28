package nl.tudelft.cse1110.grader.codechecker.integration;

import nl.tudelft.cse1110.grader.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.codechecker.engine.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class T02_M2021_IsTriangle extends IntegrationTestBase {

    private final CheckScript checkScript = script("integration/t02_m2021_isTriangle/m2021-isTriangle.yml");

    @Test
    void solution1_pass() {
        checkScript.runChecks(new TestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution1Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "10\n" +
                "should have property(ies): PASS (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: PASS (weight: 8)\n"
        );
    }

    @Test
    void solution2_fail_completely_empty() {
        checkScript.runChecks(new TestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution2Fail.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "0\n" +
                "should have property(ies): FAIL (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: FAIL (weight: 8)\n"
        );
    }

    @Test
    void solution3_pass() {
        checkScript.runChecks(new TestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution3Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "10\n" +
                "should have property(ies): PASS (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: PASS (weight: 8)\n"
        );
    }

    // this one was crashing weblab, we do not know why. It works here.
    @Test
    void solution4_pass() {
        checkScript.runChecks(new TestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution4Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "10\n" +
                "should have property(ies): PASS (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: PASS (weight: 8)\n"
    );
    }
}
