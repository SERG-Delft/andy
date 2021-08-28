package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.mode.Action;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

public class Andy {

    public static void main(String[] args) {
        if (System.getenv("ACTION") == null) {
            throw new RuntimeException("No action has been set.");
        }

        Context cfg = buildConfiguration();

        ResultBuilder result = new ResultBuilder();
        ExecutionFlow flow = buildExecutionFlow(cfg, result);

        flow.run();
        System.out.println(result.buildEndUserResult());
    }

    private static Context buildConfiguration() {
        Action action = Action.valueOf(System.getenv("ACTION"));
        Context cfg = new Context(action);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                System.getenv("WORKING_DIR"),
                System.getenv("OUTPUT_DIR")
        );
        cfg.setDirectoryConfiguration(dirCfg);
        return cfg;
    }

    private static ExecutionFlow buildExecutionFlow(Context cfg, ResultBuilder result) {
        return ExecutionFlow.justBasic(cfg, result);
    }
}
