package nl.tudelft.cse1110.grader.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.step.CompilationStep;
import nl.tudelft.cse1110.grader.execution.step.RunJUnitTests;
import nl.tudelft.cse1110.grader.execution.step.RunJacoco;
import nl.tudelft.cse1110.grader.execution.step.RunMetaTests;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GraderIntegrationTestHelper {


    public static List<ExecutionStep> justTests() {
        return Arrays.asList(new RunJUnitTests());
    }


    public static CheckScript noScript() {
        return new CheckScript(Collections.emptyList());
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

}
