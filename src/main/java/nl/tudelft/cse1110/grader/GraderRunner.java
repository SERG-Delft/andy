package nl.tudelft.cse1110.grader;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.result.GradeValues;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.util.Arrays;

public class GraderRunner {

    public static void main(String[] args) {

        // for now, only testing purposes

//        DefaultConfiguration cfg = new DefaultConfiguration(
//            "delft.NumberUtils",
//            "/Users/mauricioaniche/education/cse1110/test/code",
//            "/Users/mauricioaniche/education/cse1110/test/libs",
//            "/Users/mauricioaniche/education/cse1110/test/reports",
//                codeCheckerScript
//        );
        Configuration cfg = new Configuration();

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                "E:\\TUDelft\\CSE1110 Summer\\code",
                "E:\\TUDelft\\CSE1110 Summer\\libs",
                "E:\\TUDelft\\CSE1110 Summer\\reports"
        );

        cfg.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = new ResultBuilder();

        ExecutionFlow flow = ExecutionFlow.fullMode(cfg, result);
        flow.run();

        System.out.println(result.buildDebugResult());
    }
}
