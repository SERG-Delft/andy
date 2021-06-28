package nl.tudelft.cse1110.grader;

import nl.tudelft.cse1110.grader.config.DefaultConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.step.CompilationStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

public class GraderRunner {

    public static void main(String[] args) {

        // for now, only testing purposes
        DefaultConfiguration cfg = new DefaultConfiguration(
            "/Users/mauricioaniche/education/cse1110/test/code",
            "/Users/mauricioaniche/education/cse1110/test/libs",
            "/Users/mauricioaniche/education/cse1110/test/reports"
        );

        ResultBuilder result = new ResultBuilder();

        ExecutionFlow flow = new ExecutionFlow(new CompilationStep(), cfg, result);
        flow.run();

        System.out.println(result.buildDebugResult());
    }
}
