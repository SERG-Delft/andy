package nl.tudelft.cse1110.codechecker.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.TestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class T01_M2021_Upvote extends IntegrationTestBase {

    private final CheckScript checkScript = script("integration/t01_m2021_upvote/m2021-upvote.yml");

    @Test
    void solution1_pass() {

        checkScript.runChecks(new TestUtils().getTestResource("integration/t01_m2021_upvote/Solution1PassTest.java"));

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
        checkScript.runChecks(new TestUtils().getTestResource("integration/t01_m2021_upvote/Solution2VerifyAfterEachTest.java"));

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
        checkScript.runChecks(new TestUtils().getTestResource("integration/t01_m2021_upvote/Solution3NoVerifyTest.java"));

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
        checkScript.runChecks(new TestUtils().getTestResource("integration/t01_m2021_upvote/Solution4UnnecessarySetUps.java"));

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
