package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.config.RunConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import static nl.tudelft.cse1110.andy.grader.util.FilesUtils.findSolution;

public class RunCodeChecksStep implements ExecutionStep {
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        CheckScript script = runCfg.checkScript();
        script.runChecks(findSolution(dirCfg.getWorkingDir()));

        result.logCodeChecks(script, ctx.getModeSelector().showHints());

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunCodeChecksStep;
    }
}
