package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AndyLauncher {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Please specify an action: " + getValidActionsString());
            System.exit(-1);
        }

        String dir = System.getProperty("user.dir");

        StandardResultWriter writer = new StandardResultWriter();
        Context ctx = Context.build(getAction(args[0]), dir, dir);

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
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid action/task mode: " + action);
            System.out.println("Valid ones: " + getValidActionsString());
            System.exit(-1);
            return null;
        }
    }

    private static String getValidActionsString() {
        return Arrays.stream(Action.values()).map(a -> a.toString()).collect(Collectors.joining(","));
    }
}
