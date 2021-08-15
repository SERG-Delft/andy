package nl.tudelft.cse1110.grader.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.step.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GraderIntegrationTestHelper {


    public static List<ExecutionStep> justTests() {
        return Arrays.asList(new RunJUnitTests());
    }

    public static List<ExecutionStep> testsAndFinalGrade() {
        return Arrays.asList(new RunJUnitTests(), new CalculateFinalGradeStep());
    }

    public static CheckScript noScript() {
        return new CheckScript(Collections.emptyList());
    }

    public static List<ExecutionStep> withPiTest() {
        return Arrays.asList(new RunPitest());
    }

    public static List<ExecutionStep> justCompilation() {
        return Arrays.asList(new CompilationStep());
    }

    public static List<ExecutionStep> withJacoco() {
        return Arrays.asList(new RunJacoco());
    }

    public static List<ExecutionStep> withMeta() {
        return Arrays.asList(new RunMetaTests());
    }

    public static List<ExecutionStep> fullMode() {
        return Arrays.asList(new RunJUnitTests(),
                new RunJacoco(),
                new RunPitest(),
                new CodeChecksStep(),
                new RunMetaTests(),
                new CalculateFinalGradeStep());
    }

    public static List<ExecutionStep> examMode() {
        return Arrays.asList(new RunJUnitTests(),
                new RunJacoco(),
                new RunPitest(),
                new CalculateFinalGradeStep());
    }

}
