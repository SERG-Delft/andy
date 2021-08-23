package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;

import java.util.Collections;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;
import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Action.*;
import static nl.tudelft.cse1110.andy.grader.execution.step.helper.Mode.*;

public class ModeSelector {

    private Mode mode;
    private Action action;

    public ModeSelector(Mode mode, Action action) {
        this.mode = mode;
        this.action = action;
    }

    public List<ExecutionStep> selectMode() {
        switch (mode) {
            case PRACTICE -> {
                return getPracticeMode();
            }
            case EXAM -> {
                return getExamMode();
            }
            case GRADING -> {
                return getGradingMode();
            }
        }
        return Collections.emptyList();
    }

    public boolean showHints() {
        return mode == GRADING || action == HINTS;
    }

    private List<ExecutionStep> getPracticeMode() {
        if (action == HINTS || action == NO_HINTS) {
            return fullMode();
        } else if (action == COVERAGE) {
            return withCoverage();
        } else {
            return justTests();
        }
    }

    private List<ExecutionStep> getExamMode() {
        if (action == HINTS || action == NO_HINTS || action == COVERAGE) {
            return withCoverage();
        } else {
            return justTests();
        }
    }

    private List<ExecutionStep> getGradingMode() {
        return fullMode();
    }

    public static List<ExecutionStep> justTests() {
        return List.of(new RunJUnitTestsStep());
    }

    public static List<ExecutionStep> withCoverage() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep()
        );
    }

    public static List<ExecutionStep> fullMode() {
        return List.of(
                new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep(),
                new RunCodeChecksStep(),
                new RunMetaTestsStep(),
                new CalculateFinalGradeStep()
        );
    }

}
