package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AndyOnWebLab {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Andy needs 3 parameters: <ACTION/TASK_MODE> <WORK DIRECTORY> <OUTPUT DIRECTORY> ");
            System.exit(-1);
        }

        String action = args[0];
        String workDir = args[1];
        String outputDir = args[2];

        if (action == null) { System.out.println("No ACTION environment variable.");      System.exit(-1); }
        if (workDir == null) { System.out.println("No WORKING_DIR environment variable."); System.exit(-1); }
        if (outputDir == null) { System.out.println("No OUTPUT_DIR environment variable.");  System.exit(-1); }

        runAndy(action, workDir, outputDir);
    }

    private static void runAndy(String action, String workDir, String outputDir) {
        WebLabResultWriter writer = new WebLabResultWriter();

        ContextDirector director = new ContextDirector(new ContextBuilder());
        Context ctx = director.constructBase(
                getAction(action),
                new DirectoryConfiguration(workDir, outputDir)
        );

        try {
            Result result = new Andy().run(ctx);
            writer.write(ctx, result);
        } catch (Throwable e) {
            writer.uncaughtError(ctx, e);
        }
    }

    private static Action getAction(String action) {
        try {
            return Action.valueOf(action);
        } catch(IllegalArgumentException e) {
            System.out.println("Invalid action/task mode: " + action);
            System.out.println("Valid ones: " + Arrays.stream(Action.values()).map(Enum::toString).collect(Collectors.joining(",")));
            System.exit(-1);
            return null;
        }
    }

}
