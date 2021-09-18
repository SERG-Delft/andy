package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.weblab.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.weblab.WebLabResultWriter;

import java.util.List;

public class Andy {

    private final String action;
    private final String workDir;
    private final String outputDir;
    private final List<String> librariesToBeIncluded;

    public static void main(String[] args) {
        String action = args[0];
        String workDir = args[1];
        String outputDir = args[2];

        if (action == null) { System.out.println("No ACTION environment variable.");      System.exit(-1); }
        if (workDir == null) { System.out.println("No WORKING_DIR environment variable."); System.exit(-1); }
        if (outputDir == null) { System.out.println("No OUTPUT_DIR environment variable.");  System.exit(-1); }

        new Andy(action, workDir, outputDir, null).assess();
    }

    public Andy(String action, String workDir, String outputDir, List<String> librariesToBeIncluded) {
        assert action!=null;
        assert workDir!=null;
        assert outputDir!=null;

        this.action = action;
        this.workDir = workDir;
        this.outputDir = outputDir;
        this.librariesToBeIncluded = librariesToBeIncluded;
    }

    public void assess() {
        Context ctx = buildContext();

        ResultBuilder result = new ResultBuilder(ctx, new GradeCalculator());
        ResultWriter writer = new WebLabResultWriter(ctx, new RandomAsciiArtGenerator());
        ExecutionFlow flow = ExecutionFlow.build(ctx, result, writer);

        flow.run();
    }

    private Context buildContext() {
        Context ctx = new Context(Action.valueOf(action));

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir,
                outputDir);

        ctx.setDirectoryConfiguration(dirCfg);
        ctx.setLibrariesToBeIncluded(librariesToBeIncluded);

        return ctx;
    }

}
