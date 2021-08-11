package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import static nl.tudelft.cse1110.grader.util.FileUtils.findSolution;

public class CodeChecksStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        RunConfiguration runCfg = cfg.getRunConfiguration();

        CheckScript script = runCfg.checkScript();
        script.runChecks(findSolution(dirCfg.getWorkingDir()));

        result.logCodeChecks(script);

    }
}
