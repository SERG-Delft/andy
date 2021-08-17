package nl.tudelft.cse1110;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.step.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExecutionStepHelper {


    public static List<ExecutionStep> justTests() {
        return Arrays.asList(new RunJUnitTestsStep());
    }

    public static List<ExecutionStep> testsAndFinalGrade() {
        return Arrays.asList(new RunJUnitTestsStep(), new CalculateFinalGradeStep());
    }

    public static CheckScript noScript() {
        return new CheckScript(Collections.emptyList());
    }

    public static List<ExecutionStep> withPiTest() {
        return Arrays.asList(new RunPitestStep());
    }

    public static List<ExecutionStep> justCompilation() {
        return Arrays.asList(new CompilationStep());
    }

    public static List<ExecutionStep> justCodeChecks() {
        return Arrays.asList(new RunCodeChecksStep());
    }

    public static List<ExecutionStep> withJacoco() {
        return Arrays.asList(new RunJacocoCoverageStep());
    }

    public static List<ExecutionStep> withMeta() {
        return Arrays.asList(new RunMetaTestsStep());
    }

    public static List<ExecutionStep> fullMode() {
        return Arrays.asList(new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep(),
                new RunCodeChecksStep(),
                new RunMetaTestsStep(),
                new CalculateFinalGradeStep());
    }

    public static List<ExecutionStep> examMode() {
        return Arrays.asList(new RunJUnitTestsStep(),
                new RunJacocoCoverageStep(),
                new RunPitestStep(),
                new CalculateFinalGradeStep());
    }

}
