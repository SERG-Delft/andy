package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Action.*;
import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Mode.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeSelectorTest {

    private List<ExecutionStep> run(Mode mode, Action action) {
        ModeSelector modeSelector = new ModeSelector(mode, action);

        return modeSelector.getCorrectSteps();
    }

    @Test
    void testPracticeModeHints() {
        List<ExecutionStep> result = run(PRACTICE, HINTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeNoHints() {
        List<ExecutionStep> result = run(PRACTICE, NO_HINTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeCoverage() {
        List<ExecutionStep> result = run(PRACTICE, COVERAGE);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testPracticeModeTests() {
        List<ExecutionStep> result = run(PRACTICE, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testExamModeCoverage() {
        List<ExecutionStep> result = run(EXAM, COVERAGE);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testExamModeTests() {
        List<ExecutionStep> result = run(EXAM, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testGradingMode() {
        List<ExecutionStep> result = run(GRADING, TESTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @ParameterizedTest
    @MethodSource("generator")
    void testShowHints(Mode mode, Action action, boolean showHints) {
        ModeSelector modeSelector = new ModeSelector(mode, action);

        assertThat(modeSelector.shouldShowHints()).isEqualTo(showHints);
    }

    static Stream<Arguments> generator() {
        return Stream.of(
                Arguments.of(EXAM, TESTS, false),
                Arguments.of(GRADING, TESTS, true),
                Arguments.of(PRACTICE, HINTS, true),
                Arguments.of(GRADING, HINTS, true)
        );
    }

}
