package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeActionSelectorIntegrationTest extends IntegrationTestBase {

    @Test
    void testPracticeHints() {
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
    void testPracticeNoHints() {
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
    void testPracticeCoverage() {
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
    void testPracticeTests() {
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

    @ParameterizedTest
    @MethodSource("testExamCoverageGenerator")
    void testExamCoverageGenerator(Action action) {
        String result = run(action, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

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

    static Stream<Arguments> testExamCoverageGenerator() {
        return Stream.of(
                Arguments.of(Action.FULL_WITH_HINTS),
                Arguments.of(Action.FULL_WITHOUT_HINTS),
                Arguments.of(Action.COVERAGE)
        );
    }

    @Test
    void testExamTests() {
        String result = run(Action.TESTS, "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(noJacocoCoverage())
                .has(noPitestCoverage())
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("EXAM"));

        assertThat(resultXmlHasCorrectGrade(workDir.toString(), 0))
                .isTrue();

    }

    @Test
    void testGradingMode() {
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
