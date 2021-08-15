package nl.tudelft.cse1110.grader;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

public class GraderRunner {

    public static void main(String[] args) {
        Configuration cfg = new Configuration();

//        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
//                "E:\\TUDelft\\CSE1110 Summer\\code",
//                "E:\\TUDelft\\CSE1110 Summer\\libs",
//                "E:\\TUDelft\\CSE1110 Summer\\reports"
//        );

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                System.getenv("WORKING_DIR"),
                System.getenv("REPORTS_DIR")
        );

        cfg.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = new ResultBuilder();

//        ExecutionFlow flow = ExecutionFlow.fullMode(cfg, result);
//        flow.run();
//
//        System.out.println(result.buildDebugResult());

        String mode = System.getenv("MODE");

        ExecutionFlow flow = null;
        if (mode.equals("FULL")) {
            flow = ExecutionFlow.fullMode(cfg, result);
        } else if (mode.equals("EXAM")) {
            flow = ExecutionFlow.examMode(cfg, result);
        } else if (mode.equals("TESTS")) {
            flow = ExecutionFlow.justTests(cfg, result);
        }

        if (flow != null) {
            flow.run();

            System.out.println(result.buildDebugResult());
        } else {
            System.out.println("Unknown mode");
        }
    }
}
