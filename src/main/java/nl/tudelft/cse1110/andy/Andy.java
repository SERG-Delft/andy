package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;

import java.util.List;

public class Andy {

    private final String action;
    private final String workDir;
    private final String outputDir;
    private final List<String> librariesToBeIncluded;
    private final ResultWriter writer;

    public Andy(String action, String workDir, String outputDir, List<String> librariesToBeIncluded, ResultWriter writer) {
        this.writer = writer;
        assert action!=null;
        assert workDir!=null;
        assert outputDir!=null;

        this.action = action;
        this.workDir = workDir;
        this.outputDir = outputDir;
        this.librariesToBeIncluded = librariesToBeIncluded;
    }

    public Andy(String action, String workDir, String outputDir, ResultWriter writer) {
        this(action, workDir, outputDir, null, writer);
    }

    public void run() {
        Context ctx = buildContext();

        ResultBuilder result = new ResultBuilder(ctx, new GradeCalculator());
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
