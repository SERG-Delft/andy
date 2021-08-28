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

        logCodeChecks(ctx, result, script);
    }

    private void logCodeChecks(Context ctx, ResultBuilder result, CheckScript script) {

        if (script.hasChecks()) {
            int weightedChecks = script.weightedChecks();
            int sumOfWeights = script.weights();

            if (ctx.getModeActionSelector().shouldShowHints()) {
                result.message("\n--- Code checks");
                result.message(script.generateReportOFailedChecks().trim());

                result.message(String.format("\n%d/%d passed", weightedChecks, sumOfWeights));
            }

            result.setCheckGrade(weightedChecks, sumOfWeights);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunCodeChecksStep;
    }
}
