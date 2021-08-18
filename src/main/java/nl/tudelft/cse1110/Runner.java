package nl.tudelft.cse1110;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Runner {

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        Configuration cfg = buildConfiguration(startTime);

        ResultBuilder result = new ResultBuilder();
        ExecutionFlow flow = buildExecutionFlow(cfg, result);

        if(flow == null) {
            System.out.println("Unknown mode");
            System.exit(-1);
        }

        flow.run();
        System.out.println(result.buildDebugResult());
    }

    @NotNull
    private static Configuration buildConfiguration(long startTime) {
        Configuration cfg = new Configuration();
        cfg.setStartTime(startTime);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                System.getenv("WORKING_DIR"),
                System.getenv("OUTPUT_DIR")
        );
        cfg.setDirectoryConfiguration(dirCfg);
        return cfg;
    }

    @Nullable
    private static ExecutionFlow buildExecutionFlow(Configuration cfg, ResultBuilder result) {
        String mode = System.getenv("MODE");
        ExecutionFlow flow = null;
        if (mode.equals("FULL")) {
            flow = ExecutionFlow.fullMode(cfg, result);
        } else if (mode.equals("EXAM")) {
            flow = ExecutionFlow.examMode(cfg, result);
        } else if (mode.equals("TESTS")) {
            flow = ExecutionFlow.justTests(cfg, result);
        }
        return flow;
    }
}
