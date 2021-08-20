package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.grader.config.Configuration;
import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.config.RunConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import static nl.tudelft.cse1110.andy.grader.util.FileUtils.findSolution;

public class RunCodeChecksStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        RunConfiguration runCfg = cfg.getRunConfiguration();

        CheckScript script = runCfg.checkScript();
        script.runChecks(findSolution(dirCfg.getWorkingDir()));

        result.logCodeChecks(script);

    }
}
