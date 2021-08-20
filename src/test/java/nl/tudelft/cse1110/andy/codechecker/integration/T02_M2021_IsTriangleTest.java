package nl.tudelft.cse1110.andy.codechecker.integration;

import nl.tudelft.cse1110.andy.codechecker.CodeCheckerTestUtils;
import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.AndCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class T02_M2021_IsTriangleTest {

    private final CheckScript checkScript = new CheckScript(Arrays.asList(
            new SingleCheck(2, "should have property(ies)", new JQWikProperty(Comparison.GTE, 1)),
            new OrCheck(8, "either make use of Arbitraries or JQWik IntRange-like annotations", Arrays.asList(
                    new AndCheck(Arrays.asList(
                            new SingleCheck(new JQWikArbitrary()),
                            new SingleCheck(new JQWikProvide(Comparison.GTE, 1))
                    )),
                    new AndCheck(Arrays.asList(
                            new SingleCheck(new JQWikProvide(Comparison.EQ, 0)),
                            new SingleCheck(new JQWikProperty(Comparison.GTE, 3)),
                            new SingleCheck(new JQWikProvideAnnotations())
                    ))
            ))
    ));

    @Test
    void solution1_pass() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution1Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "10\n" +
                "should have property(ies): PASS (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: PASS (weight: 8)\n"
        );
    }

    @Test
    void solution2_fail_completely_empty() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution2Fail.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "0\n" +
                "should have property(ies): FAIL (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: FAIL (weight: 8)\n"
        );
    }

    @Test
    void solution3_pass() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution3Pass.java"));

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
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t02_m2021_isTriangle/Solution4Pass.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "10\n" +
                "10\n" +
                "should have property(ies): PASS (weight: 2)\n" +
                "either make use of Arbitraries or JQWik IntRange-like annotations: PASS (weight: 8)\n"
        );
    }
}
