package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

public class Andy {

    public static void main(String[] args) {
        Context cfg = buildConfiguration();

        ResultBuilder result = new ResultBuilder();
        ExecutionFlow flow = buildExecutionFlow(cfg, result);

        if(flow == null) {
            System.out.println("Unknown mode");
            System.exit(-1);
        }

        flow.run();
        System.out.println(result.buildEndUserResult());
    }

    private static Context buildConfiguration() {
        Context cfg = new Context();

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                System.getenv("WORKING_DIR"),
                System.getenv("OUTPUT_DIR")
        );
        cfg.setDirectoryConfiguration(dirCfg);
        return cfg;
    }

    private static ExecutionFlow buildExecutionFlow(Context cfg, ResultBuilder result) {
        ExecutionFlow flow = ExecutionFlow.justBasic(cfg, result);
        cfg.setFlow(flow);
        return flow;
    }
}
