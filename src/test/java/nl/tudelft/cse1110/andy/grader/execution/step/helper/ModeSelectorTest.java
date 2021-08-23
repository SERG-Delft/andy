package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;

public class ModeSelectorTest {

    private List<ExecutionStep> run(String mode, String environmentMode) {
        ModeSelector modeSelector = new ModeSelector(mode, environmentMode);

        return modeSelector.selectMode();
    }

    @Test
    void testPracticeModeHints() {
        List<ExecutionStep> result = run(PRACTICE_MODE, "HINTS");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeNoHints() {
        List<ExecutionStep> result = run(PRACTICE_MODE, "NO_HINTS");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeCoverage() {
        List<ExecutionStep> result = run(PRACTICE_MODE, "COVERAGE");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testPracticeModeTests() {
        List<ExecutionStep> result = run(PRACTICE_MODE, "TESTS");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testExamModeCoverage() {
        List<ExecutionStep> result = run(EXAM_MODE, "COVERAGE");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testExamModeTests() {
        List<ExecutionStep> result = run(EXAM_MODE, "TESTS");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testGradingMode() {
        List<ExecutionStep> result = run(GRADING_MODE, "");

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

}
