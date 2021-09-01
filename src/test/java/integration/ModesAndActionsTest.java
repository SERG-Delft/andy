package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static testutils.WebLabTestAssertions.*;

public class ModesAndActionsTest extends IntegrationTestBase {

    @Test
    void showAllHintsWhenInPracticeMode() {
        String result = run(Action.FULL_WITH_HINTS,"SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(scoreOfCodeChecks(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1))
                .has(metaTests(4))
                .has(metaTestsPassing(3))
                .has(metaTestFailing("DoesNotCheckInvalidTripId"))
                .has(finalGrade(workDir.toString(), 91))
                .has(mode("PRACTICE"));
    }

    @Test
    void showPartialHintsWhenInPracticeMode() {
        String result = run(Action.FULL_WITHOUT_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(metaTestsButNoDetails())
                .has(codeChecksButNoDetails())
                .has(finalGrade(workDir.toString(), 91))
                .has(mode("PRACTICE"));
    }

    @Test
    void showCoverage() {
        String result = run(Action.COVERAGE, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("PRACTICE"));

        assertThat(resultXmlHasCorrectGrade(workDir.toString(), 0))
                .isTrue();
    }

    @Test
    void onlyTheTestsRun() {
        String result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(noJacocoCoverage())
                .has(noPitestCoverage())
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("PRACTICE"));

        assertThat(resultXmlHasCorrectGrade(workDir.toString(), 0))
                .isTrue();
    }

    @Test
    void showOnlyTestsRunAndCoverageDuringExam() {
        String result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("EXAM"));

        assertThat(resultXmlHasCorrectGrade(workDir.toString(), 0))
                .isTrue();

    }

    @Test
    void gradingModeShouldRunEverything() {
        String result = run(Action.FULL_WITH_HINTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksGrading");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(scoreOfCodeChecks(3,3))
                .has(codeCheck("Trip Repository should be mocked", true, 1))
                .has(codeCheck("Trip should not be mocked", true, 1))
                .has(codeCheck("getTripById should be set up", true, 1))
                .has(metaTests(4))
                .has(metaTestsPassing(3))
                .has(metaTestFailing("DoesNotCheckInvalidTripId"))
                .has(finalGrade(workDir.toString(), 91))
                .has(mode("GRADING"));
    }

}
