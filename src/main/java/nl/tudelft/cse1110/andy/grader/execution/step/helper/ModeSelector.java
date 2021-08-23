package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;
import org.dom4j.rule.Mode;

import java.util.Collections;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.config.RunConfiguration.*;
import static nl.tudelft.cse1110.andy.grader.util.ModeUtils.*;

public class ModeSelector {

    private String runMode;
    private String environmentMode;

    public ModeSelector(String runMode, String environmentMode) {
        this.runMode = runMode;
        this.environmentMode = environmentMode;
    }

    public List<ExecutionStep> selectMode() {
        switch (runMode) {
            case PRACTICE_MODE -> {
                if (hints(environmentMode) || noHints(environmentMode)) {
                    return fullMode();
                } else if (coverage(environmentMode)) {
                    return withCoverage();
                } else {
                    return justTests();
                }
            }
            case EXAM_MODE -> {
                if (coverage(environmentMode)) {
                    return withCoverage();
                } else {
                    return justTests();
                }
            }
            case GRADING_MODE -> {
                return fullMode();
            }
        }
        return Collections.emptyList();
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
