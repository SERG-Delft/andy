package nl.tudelft.cse1110.andy.execution.mode;

import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.execution.mode.Action.*;
import static nl.tudelft.cse1110.andy.execution.mode.Mode.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeActionSelectorUnitTest {

    private List<ExecutionStep> run(Mode mode, Action action) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        return modeActionSelector.getSteps();
    }

    @Test
    void testPracticeModeHints() {
        List<ExecutionStep> result = run(PRACTICE, FULL_WITH_HINTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.fullMode());
    }

    @Test
    void testPracticeModeNoHints() {
        List<ExecutionStep> result = run(PRACTICE, FULL_WITHOUT_HINTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.fullMode());
    }

    @Test
    void testPracticeModeCoverage() {
        List<ExecutionStep> result = run(PRACTICE, COVERAGE);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.withCoverage());
    }

    @Test
    void testPracticeModeTests() {
        List<ExecutionStep> result = run(PRACTICE, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.justTests());
    }

    @Test
    void testExamModeCoverage() {
        List<ExecutionStep> result = run(EXAM, COVERAGE);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.withCoverage());
    }

    @Test
    void testExamModeTests() {
        List<ExecutionStep> result = run(EXAM, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.justTests());
    }

    @Test
    void testGradingMode() {
        List<ExecutionStep> result = run(GRADING, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.fullMode());
    }

    @ParameterizedTest
    @MethodSource("testShowHintsGenerator")
    void testShowHints(Mode mode, Action action, boolean showHints) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        assertThat(modeActionSelector.shouldShowHints()).isEqualTo(showHints);
    }

    static Stream<Arguments> testShowHintsGenerator() {
        return Stream.of(
                Arguments.of(EXAM, TESTS, false),
                Arguments.of(GRADING, TESTS, true),
                Arguments.of(PRACTICE, FULL_WITH_HINTS, true),
                Arguments.of(GRADING, FULL_WITH_HINTS, true),
                Arguments.of(EXAM, CUSTOM, true)
        );
    }

    @ParameterizedTest
    @MethodSource("testShowGradesGenerator")
    void shouldShowGrades(Mode mode, Action action, boolean shouldShowGradeOrNot) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        assertThat(modeActionSelector.shouldCalculateAndShowGrades()).isEqualTo(shouldShowGradeOrNot);
    }

    static Stream<Arguments> testShowGradesGenerator() {
        return Stream.of(
                Arguments.of(EXAM, TESTS, false),
                Arguments.of(EXAM, COVERAGE, false),
                Arguments.of(EXAM, FULL_WITH_HINTS, false),
                Arguments.of(EXAM, FULL_WITHOUT_HINTS, false),
                Arguments.of(PRACTICE, TESTS, false),
                Arguments.of(PRACTICE, COVERAGE, false),
                Arguments.of(PRACTICE, FULL_WITH_HINTS, true),
                Arguments.of(PRACTICE, FULL_WITHOUT_HINTS, true),
                // grading will only be called with full mode
                Arguments.of(GRADING, FULL_WITH_HINTS, true)
        );
    }

    @ParameterizedTest
    @MethodSource("shouldGenerateAnalyticsGenerator")
    void shouldGenerateAnalytics(Mode mode, Action action, boolean expectedResult) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        assertThat(modeActionSelector.shouldGenerateAnalytics()).isEqualTo(expectedResult);
    }

    static Stream<Arguments> shouldGenerateAnalyticsGenerator() {
        return Stream.of(
                // not during exam
                Arguments.of(EXAM, TESTS, false),
                Arguments.of(EXAM, COVERAGE, false),
                Arguments.of(EXAM, FULL_WITH_HINTS, false),
                Arguments.of(EXAM, FULL_WITHOUT_HINTS, false),

                // not when custom
                Arguments.of(EXAM, CUSTOM, false),
                Arguments.of(GRADING, CUSTOM, false),
                Arguments.of(PRACTICE, CUSTOM, false),

                // always during practice
                Arguments.of(PRACTICE, TESTS, true),
                Arguments.of(PRACTICE, COVERAGE, true),
                Arguments.of(PRACTICE, FULL_WITH_HINTS, true),
                Arguments.of(PRACTICE, FULL_WITHOUT_HINTS, true),

                // never during grading
                Arguments.of(GRADING, FULL_WITH_HINTS, false)
        );
    }

}
