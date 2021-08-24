package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Action.*;
import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Mode.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ModeActionSelectorTest {

    private List<ExecutionStep> run(Mode mode, Action action) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        return modeActionSelector.getCorrectSteps();
    }

    @Test
    void testPracticeModeHints() {
        List<ExecutionStep> result = run(PRACTICE, HINTS);

        assertThat(result)
                .containsExactlyElementsOf(ModeActionSelector.fullMode());
    }

    @Test
    void testPracticeModeNoHints() {
        List<ExecutionStep> result = run(PRACTICE, NO_HINTS);

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
    @MethodSource("generator")
    void testShowHints(Mode mode, Action action, boolean showHints) {
        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);

        assertThat(modeActionSelector.shouldShowHints()).isEqualTo(showHints);
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
