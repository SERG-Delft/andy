package nl.tudelft.cse1110.andy.codechecker.integration;

import nl.tudelft.cse1110.andy.codechecker.CodeCheckerTestUtils;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikArbitraries;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikProperty;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikProvide;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class T03_M2021_IsLeapYear {

    private final CheckScript checkScript = new CheckScript(Arrays.asList(
            new SingleCheck("should have at least 2 properties", new JQWikProperty(Comparison.GTE, 2)),
            new SingleCheck("should have at least 2 provides", new JQWikProvide(Comparison.GTE, 2)),
            new SingleCheck(2, "should make use of Arbitraries.integers()", new JQWikArbitraries("integers"))
    ));

    @Test
    void solution1_pass() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t03_m2021_isLeapYear/Solution1Pass.java"));

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
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t03_m2021_isLeapYear/Solution2Fail.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                        "4\n" +
                        "2\n" +
                        "should have at least 2 properties: PASS (weight: 1)\n" +
                        "should have at least 2 provides: PASS (weight: 1)\n" +
                        "should make use of Arbitraries.integers(): FAIL (weight: 2)\n"
        );
    }

}
