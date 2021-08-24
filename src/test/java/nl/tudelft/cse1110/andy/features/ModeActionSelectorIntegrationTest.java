package nl.tudelft.cse1110.andy.features;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.Action;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.*;
import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeActionSelectorIntegrationTest extends IntegrationTestBase {

    @Test
    void testPracticeHints() {
        String result = run(Action.HINTS, onlyBasic(),"SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

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
                .has(finalGrade(91))
                .has(mode("PRACTICE"));
    }

    @Test
    void testPracticeNoHints() {
        String result = run(Action.NO_HINTS, onlyBasic(), "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(finalGrade(91))
                .has(mode("PRACTICE"));
    }

    @Test
    void testPracticeCoverage() {
        String result = run(Action.COVERAGE, onlyBasic(), "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("PRACTICE"));
    }

    @Test
    void testPracticeTests() {
        String result = run(Action.TESTS, onlyBasic(), "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecks");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(noJacocoCoverage())
                .has(noPitestCoverage())
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("PRACTICE"));
    }

    @ParameterizedTest
    @MethodSource("testExamCoverageGenerator")
    void testExamCoverageGenerator(Action action) {
        String result = run(action, onlyBasic(),"SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(linesCovered(11))
                .has(mutationScore(8, 9))
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("EXAM"));
    }

    static Stream<Arguments> testExamCoverageGenerator() {
        return Stream.of(
                Arguments.of(Action.HINTS),
                Arguments.of(Action.NO_HINTS),
                Arguments.of(Action.COVERAGE)
        );
    }

    @Test
    void testExamTests() {
        String result = run(Action.TESTS, onlyBasic(),"SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksExam");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(noJacocoCoverage())
                .has(noPitestCoverage())
                .has(noMetaTests())
                .has(noCodeChecks())
                .has(noFinalGrade())
                .has(mode("EXAM"));
    }

    @Test
    void testGradingMode() {
        String result = run(Action.TESTS, onlyBasic(),"SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfigMetaAndCodeChecksGrading");

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
                .has(finalGrade(91))
                .has(mode("GRADING"));

    }

}
