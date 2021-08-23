package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;
import org.dom4j.rule.Mode;

import java.util.Collections;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;

public class ModeSelector {

    private String runMode;
    private String environmentMode;

    public static final String HINTS = "HINTS";
    public static final String NO_HINTS = "NO_HINTS";
    public static final String COVERAGE = "COVERAGE";
    public static final String TESTS = "TESTS";

    public ModeSelector(String runMode, String environmentMode) {
        this.runMode = runMode;
        this.environmentMode = environmentMode;
    }

    public List<ExecutionStep> selectMode() {
        switch (runMode) {
            case PRACTICE_MODE -> {
                return getPracticeMode();
            }
            case EXAM_MODE -> {
                return getExamMode();
            }
            case GRADING_MODE -> {
                return getGradingMode();
            }
        }
        return Collections.emptyList();
    }

    private List<ExecutionStep> getPracticeMode() {
        if (environmentMode.equals(HINTS) || environmentMode.equals(NO_HINTS)) {
            return fullMode();
        } else if (environmentMode.equals(COVERAGE)) {
            return withCoverage();
        } else {
            return justTests();
        }
    }

    private List<ExecutionStep> getExamMode() {
        if (environmentMode.equals(COVERAGE)
                || environmentMode.equals(HINTS)
                || environmentMode.equals(NO_HINTS)) {
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
