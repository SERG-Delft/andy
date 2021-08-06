package nl.tudelft.cse1110.grader;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.grader.config.DefaultConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.util.Arrays;

public class GraderRunner {

    public static void main(String[] args) {

        // for now, only testing purposes
        CheckScript codeCheckerScript = new CheckScript(Arrays.asList(new SingleCheck("TestMethodsHaveAssertions")));

        DefaultConfiguration cfg = new DefaultConfiguration(
                "delft.NumberUtils",
                "E:\\TUDelft\\CSE1110 Summer\\code",
                "E:\\TUDelft\\CSE1110 Summer\\libs",
                "E:\\TUDelft\\CSE1110 Summer\\reports",
                codeCheckerScript
        );

        ResultBuilder result = new ResultBuilder();

        ExecutionFlow flow = ExecutionFlow.fullMode(cfg, result);
        flow.run();

        System.out.println(result.buildDebugResult());
    }
}
