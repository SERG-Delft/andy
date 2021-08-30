package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

public class Andy {

    public static void main(String[] args) {
        if (System.getenv("ACTION") == null || System.getenv("WORKING_DIR") == null || System.getenv("OUTPUT_DIR") == null) {
            System.out.println("Missing configuration.");
            System.exit(-1);
        }

        Context ctx = buildConfiguration();

        ResultBuilder result = new ResultBuilder(ctx, new GradeCalculator());
        ResultWriter writer = new WebLabResultWriter(ctx, new RandomAsciiArtGenerator());
        ExecutionFlow flow = ExecutionFlow.build(ctx, result, writer);

        flow.run();
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

}
