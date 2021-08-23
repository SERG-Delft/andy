package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.RunJUnitTestsStep;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.ModeSelector;
import nl.tudelft.cse1110.andy.grader.util.ModeUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;

public class ModeSelectorTest {

    private List<ExecutionStep> run(String mode, boolean hints, boolean noHints, boolean coverage) {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);

        when(ModeUtils.hints()).thenReturn(hints);
        when(ModeUtils.noHints()).thenReturn(noHints);
        when(ModeUtils.coverage()).thenReturn(coverage);

        ModeSelector modeSelector = new ModeSelector(mode);
        List<ExecutionStep> result = modeSelector.selectMode();

        mockStatic.close();

        return result;
    }

    @Test
    void testPracticeModeHints() {
        List<ExecutionStep> result = run(PRACTICE_MODE, true, false, false);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeNoHints() {
        List<ExecutionStep> result = run(PRACTICE_MODE, false, true, false);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

    @Test
    void testPracticeModeCoverage() {
        List<ExecutionStep> result = run(PRACTICE_MODE, false, false, true);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testPracticeModeTests() {
        List<ExecutionStep> result = run(PRACTICE_MODE, false, false, false);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testExamModeCoverage() {
        List<ExecutionStep> result = run(EXAM_MODE, false, false, true);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.withCoverage());
    }

    @Test
    void testExamModeTests() {
        List<ExecutionStep> result = run(EXAM_MODE, false, false, false);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.justTests());
    }

    @Test
    void testGradingMode() {
        List<ExecutionStep> result = run(GRADING_MODE, false, false, false);

        assertThat(result)
                .containsExactlyElementsOf(ModeSelector.fullMode());
    }

}
