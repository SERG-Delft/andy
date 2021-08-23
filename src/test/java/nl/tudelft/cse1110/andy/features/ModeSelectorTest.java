package nl.tudelft.cse1110.andy.features;

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

public class ModeSelectorTest extends IntegrationTestBase {

    @Test
    void testPracticeModeHints() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.hints()).thenReturn(true);

        ModeSelector modeSelector = new ModeSelector(PRACTICE_MODE);
        List<ExecutionStep> expected = modeSelector.fullMode();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testPracticeModeNoHints() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.hints()).thenReturn(false);
        when(ModeUtils.noHints()).thenReturn(true);

        ModeSelector modeSelector = new ModeSelector(PRACTICE_MODE);
        List<ExecutionStep> expected = modeSelector.fullMode();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testPracticeModeCoverage() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.coverage()).thenReturn(true);
        when(ModeUtils.hints()).thenReturn(false);
        when(ModeUtils.noHints()).thenReturn(false);

        ModeSelector modeSelector = new ModeSelector(PRACTICE_MODE);
        List<ExecutionStep> expected = modeSelector.withCoverage();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testPracticeModeTests() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.coverage()).thenReturn(false);
        when(ModeUtils.hints()).thenReturn(false);
        when(ModeUtils.noHints()).thenReturn(false);

        ModeSelector modeSelector = new ModeSelector(PRACTICE_MODE);
        List<ExecutionStep> expected = modeSelector.justTests();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testExamModeCoverage() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.coverage()).thenReturn(true);

        ModeSelector modeSelector = new ModeSelector(EXAM_MODE);
        List<ExecutionStep> expected = modeSelector.withCoverage();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testExamModeTests() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);
        when(ModeUtils.coverage()).thenReturn(false);

        ModeSelector modeSelector = new ModeSelector(EXAM_MODE);
        List<ExecutionStep> expected = modeSelector.justTests();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

    @Test
    void testGradingMode() {
        MockedStatic<ModeUtils> mockStatic = mockStatic(ModeUtils.class);

        ModeSelector modeSelector = new ModeSelector(GRADING_MODE);
        List<ExecutionStep> expected = modeSelector.fullMode();

        assertThat(modeSelector.selectMode())
                .containsExactlyElementsOf(expected);

        mockStatic.close();
    }

}
