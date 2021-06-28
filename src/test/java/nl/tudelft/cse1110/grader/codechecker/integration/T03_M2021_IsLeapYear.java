package nl.tudelft.cse1110.grader.codechecker.integration;

import nl.tudelft.cse1110.grader.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.codechecker.engine.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class T03_M2021_IsLeapYear extends IntegrationTestBase {

    private final CheckScript checkScript = script("integration/t03_m2021_isLeapYear/m2021-isLeapYear.yml");

    @Test
    void solution1_pass() {
        checkScript.runChecks(new TestUtils().getFixtureFilePath("integration/t03_m2021_isLeapYear/Solution1Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "4\n" +
                "4\n" +
                "should have at least 2 properties: PASS (weight: 1)\n" +
                "should have at least 2 provides: PASS (weight: 1)\n" +
                "should make use of Arbitraries.integers(): PASS (weight: 2)\n"
        );
    }

    @Test
    void solution2_fail() {
        checkScript.runChecks(new TestUtils().getFixtureFilePath("integration/t03_m2021_isLeapYear/Solution2Fail.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                        "4\n" +
                        "2\n" +
                        "should have at least 2 properties: PASS (weight: 1)\n" +
                        "should have at least 2 provides: PASS (weight: 1)\n" +
                        "should make use of Arbitraries.integers(): FAIL (weight: 2)\n"
        );
    }

}
