package nl.tudelft.cse1110.grader.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.step.RunJUnitTests;

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
}
