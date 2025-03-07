package integration;

import nl.tudelft.cse1110.andy.result.Result;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;

public class CodeChecksTest extends IntegrationTestBase {

    @Test
    void allChecksPass() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecksConfiguration");

        assertThat(result)
                .has(checksScore(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void mockChecksWithAnnotationPass() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTestsWithAnnotation", "SoftWhereConfigWithCodeChecksConfiguration");

        assertThat(result)
                .has(checksScore(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void someChecksFail() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecks2Configuration");

        assertThat(result)
                .has(checksScore(2,5))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void zeroWeightCodeChecks() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithCodeChecksZeroWeight");

        assertThat(result.hasGenericFailure()).isFalse();

        assertThat(result)
                .has(checksScore(0,0))
                .has(codeCheck("Trip Repository should be mocked", true, 0))
                .has(codeCheck("Trip should be mocked", false, 0)) // this check makes no sense, just for the check to fail
                .has(codeCheck("getTripById should be set up", true, 0));
    }

    @Test
    void penaltyCodeChecksPass() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithPenaltyCodeChecksPassingConfiguration");

        assertThat(result)
                .has(checksScore(2,5))
                .has(penaltyCodeCheck("Trip Repository should be mocked penalty", true, 1))
                .has(penaltyCodeCheck("getTripById should be set up penalty", true, 1))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should be mocked", false, 3)) // this check makes no sense, just for the check to fail
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void penaltyCodeChecksFail() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigWithPenaltyCodeChecksFailingConfiguration");

        assertThat(result)
                .has(checksScore(2,5))
                .has(penaltyCodeCheck("Trip Repository should be mocked penalty", true, 1))
                .has(penaltyCodeCheck("getTripById should be set up req", true, 200))
                .has(penaltyCodeCheck("Trip should be mocked required", false, 100))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should be mocked", false, 3))
                .has(codeCheck("getTripById should be set up", true, 1));
    }

    @Test
    void noChecks() {
        Result result = run( "SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfiguration");
        assertThat(result.getCodeChecks().hasChecks()).isFalse();
    }

    public static Condition<Result> checksScore(int passed, int total) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                boolean testsPassed = value.getCodeChecks().getNumberOfPassedChecks() == passed;
                boolean totalChecks = value.getCodeChecks().getTotalNumberOfChecks() == total;

                return testsPassed && totalChecks;
            }
        };
    }

    public static Condition<Result> codeCheck(String name, boolean pass, int weight) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCodeChecks().getCheckResults().stream().anyMatch(cc ->
                        cc.getWeight() == weight &&
                                cc.passed() == pass &&
                                cc.getDescription().equals(name));

            }
        };
    }

    public static Condition<Result> penaltyCodeCheck(String name, boolean pass, int penalty) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getPenaltyCodeChecks().getCheckResults().stream().anyMatch(cc ->
                        cc.getWeight() == penalty &&
                                cc.passed() == pass &&
                                cc.getDescription().equals(name));

            }
        };
    }


}
