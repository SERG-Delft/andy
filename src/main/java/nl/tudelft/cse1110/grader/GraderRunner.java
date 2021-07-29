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
            "/Users/mauricioaniche/education/cse1110/test/code",
            "/Users/mauricioaniche/education/cse1110/test/libs",
            "/Users/mauricioaniche/education/cse1110/test/reports",
                codeCheckerScript
        );

        ResultBuilder result = new ResultBuilder(true,
                0.25f, 0.25f, 0.25f, 0.25f);

        ExecutionFlow flow = ExecutionFlow.fullMode(cfg, result);
        flow.run();

        System.out.println(result.buildDebugResult());
    }
}
