package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExecutionStepHelper {

    public static List<ExecutionStep> onlyJUnitTests() {
        return Arrays.asList(new RunJUnitTestsStep());
    }

    public static List<ExecutionStep> onlyMutationCoverage() {
        return Arrays.asList(new RunPitestStep());
    }

    public static List<ExecutionStep> onlyCompilation() {
        // compilation is part of the basic steps
        return Collections.emptyList();
    }

    public static List<ExecutionStep> onlyCodeChecks() {
        return Arrays.asList(new RunCodeChecksStep());
    }

    public static List<ExecutionStep> onlyBranchCoverage() {
        return Arrays.asList(new RunJacocoCoverageStep());
    }

    public static List<ExecutionStep> onlyMetaTests() {
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


}
