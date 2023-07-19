package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.io.FileNotFoundException;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.findSolution;

public class RunPenaltyCodeChecksStep implements ExecutionStep {
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        CheckScript script = runCfg.penaltyCheckScript();
        try {
            script.runChecks(findSolution(dirCfg.getWorkingDir()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        result.logPenaltyCodeChecks(script);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunPenaltyCodeChecksStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
