package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;

public class AndyLauncher {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Please specify an action, input dir, and output dir");
            System.exit(-1);
        }

        Action action = getAction(args[0]);
        String inputDir = args[1];
        String outputDir = args[2];

        ContextDirector director = new ContextDirector(new ContextBuilder());
        Context ctx = director.constructBase(action, new DirectoryConfiguration(inputDir, outputDir));

        StandardResultWriter writer = new StandardResultWriter();
        try {
            System.out.println("Starting Andy...");
            System.out.println(String.format("Action: %s", action));
            System.out.println(String.format("Input directory: %s", inputDir));
            System.out.println(String.format("Output directory: %s", outputDir));
            Result result = new Andy().run(ctx);

            System.out.println("Done... Writing output!");
            writer.write(ctx, result);
        } catch (Throwable e) {
            writer.uncaughtError(ctx, e);
        }

        System.out.println("Bye!");
    }

    private static Action getAction(String action) {
        try {
            return Action.valueOf(action);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid action/task mode: " + action);
            System.exit(-1);
            return null;
        }
    }
}
