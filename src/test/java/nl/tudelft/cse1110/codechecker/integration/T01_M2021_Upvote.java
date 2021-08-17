package nl.tudelft.cse1110.codechecker.integration;

import nl.tudelft.cse1110.codechecker.checks.*;
import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.CodeCheckerTestUtils;
import nl.tudelft.cse1110.codechecker.engine.OrCheck;
import nl.tudelft.cse1110.codechecker.engine.SingleCheck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class T01_M2021_Upvote {

    private final CheckScript checkScript = new CheckScript(Arrays.asList(
        new SingleCheck("UserRepository should be mocked", new MockClass("UserRepository")),
            new SingleCheck("Scoring should be mocked", new MockClass("Scoring")),
            new SingleCheck("StackOverflow should not be mocked", true, new MockClass("StackOverflow")),
            new SingleCheck("Post should not be mocked", true, new MockClass("Post")),
            new SingleCheck("Spies should not be used", true, new MockitoSpy()),
            new SingleCheck(2,"pointsForFeaturedPost should be set up", new MockitoWhen("pointsForFeaturedPost", Comparison.GTE, 1)),
            new SingleCheck(2,"pointsForNormalPost should be set up", new MockitoWhen("pointsForNormalPost", Comparison.GTE, 1)),
            new SingleCheck("set up pointsForFeaturedPost just once", new MockitoWhen("pointsForFeaturedPost", Comparison.EQ, 1)),
            new SingleCheck("set up pointsForNormalPost just once", new MockitoWhen("pointsForFeaturedPost", Comparison.EQ, 1)),
            new OrCheck(3, "update should be verified in both tests", Arrays.asList(
                    new SingleCheck(new MockitoVerify("update", "TEST", Comparison.EQ, 2)),
                    new SingleCheck(new MockitoVerify("update", "AFTEREACH", Comparison.EQ, 1))
            )),
            new SingleCheck("tests should have assertions", new TestMethodsHaveAssertions()),
            new SingleCheck(3, "getPoints should have an assertion", new MethodCalledInTestMethod("getPoints"))
    ));

    @Test
    void solution1_pass() {

        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t01_m2021_upvote/Solution1PassTest.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                        "18\n" +
                        "18\n" +
                        "UserRepository should be mocked: PASS (weight: 1)\n" +
                        "Scoring should be mocked: PASS (weight: 1)\n" +
                        "StackOverflow should not be mocked: PASS (weight: 1)\n" +
                        "Post should not be mocked: PASS (weight: 1)\n" +
                        "Spies should not be used: PASS (weight: 1)\n" +
                        "pointsForFeaturedPost should be set up: PASS (weight: 2)\n" +
                        "pointsForNormalPost should be set up: PASS (weight: 2)\n" +
                        "set up pointsForFeaturedPost just once: PASS (weight: 1)\n" +
                        "set up pointsForNormalPost just once: PASS (weight: 1)\n" +
                        "update should be verified in both tests: PASS (weight: 3)\n" +
                        "tests should have assertions: PASS (weight: 1)\n" +
                        "getPoints should have an assertion: PASS (weight: 3)\n"
        );
    }

    @Test
    void solution2_passAfterEach() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t01_m2021_upvote/Solution2VerifyAfterEachTest.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "18\n" +
                "18\n" +
                "UserRepository should be mocked: PASS (weight: 1)\n" +
                "Scoring should be mocked: PASS (weight: 1)\n" +
                "StackOverflow should not be mocked: PASS (weight: 1)\n" +
                "Post should not be mocked: PASS (weight: 1)\n" +
                "Spies should not be used: PASS (weight: 1)\n" +
                "pointsForFeaturedPost should be set up: PASS (weight: 2)\n" +
                "pointsForNormalPost should be set up: PASS (weight: 2)\n" +
                "set up pointsForFeaturedPost just once: PASS (weight: 1)\n" +
                "set up pointsForNormalPost just once: PASS (weight: 1)\n" +
                "update should be verified in both tests: PASS (weight: 3)\n" +
                "tests should have assertions: PASS (weight: 1)\n" +
                "getPoints should have an assertion: PASS (weight: 3)\n"
        );
    }

    @Test
    void solution3_fail_noVerifyForUpdate() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t01_m2021_upvote/Solution3NoVerifyTest.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                    "18\n" +
                    "15\n" +
                    "UserRepository should be mocked: PASS (weight: 1)\n" +
                    "Scoring should be mocked: PASS (weight: 1)\n" +
                    "StackOverflow should not be mocked: PASS (weight: 1)\n" +
                    "Post should not be mocked: PASS (weight: 1)\n" +
                    "Spies should not be used: PASS (weight: 1)\n" +
                    "pointsForFeaturedPost should be set up: PASS (weight: 2)\n" +
                    "pointsForNormalPost should be set up: PASS (weight: 2)\n" +
                    "set up pointsForFeaturedPost just once: PASS (weight: 1)\n" +
                    "set up pointsForNormalPost just once: PASS (weight: 1)\n" +
                    "update should be verified in both tests: FAIL (weight: 3)\n" +
                    "tests should have assertions: PASS (weight: 1)\n" +
                    "getPoints should have an assertion: PASS (weight: 3)\n"
        );
    }

    @Test
    void solution4_unnecessaryVerifies() {
        checkScript.runChecks(new CodeCheckerTestUtils().getTestResource("integration/t01_m2021_upvote/Solution4UnnecessarySetUps.java"));

        assertThat(checkScript.generateReport()).isEqualTo(
                "18\n" +
                "16\n" +
                "UserRepository should be mocked: PASS (weight: 1)\n" +
                "Scoring should be mocked: PASS (weight: 1)\n" +
                "StackOverflow should not be mocked: PASS (weight: 1)\n" +
                "Post should not be mocked: PASS (weight: 1)\n" +
                "Spies should not be used: PASS (weight: 1)\n" +
                "pointsForFeaturedPost should be set up: PASS (weight: 2)\n" +
                "pointsForNormalPost should be set up: PASS (weight: 2)\n" +
                "set up pointsForFeaturedPost just once: FAIL (weight: 1)\n" +
                "set up pointsForNormalPost just once: FAIL (weight: 1)\n" +
                "update should be verified in both tests: PASS (weight: 3)\n" +
                "tests should have assertions: PASS (weight: 1)\n" +
                "getPoints should have an assertion: PASS (weight: 3)\n"
        );
    }


}
