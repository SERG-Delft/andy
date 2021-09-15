package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

public class RunSeleniumWebPageStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            RunConfiguration runConfiguration = ctx.getRunConfiguration();
            DirectoryConfiguration directoryConfiguration = ctx.getDirectoryConfiguration();

            String externalProcess = runConfiguration.externalProcess();

            if (externalProcess == null || externalProcess.equals("")) {
                return;
            }

            Process process = Runtime.getRuntime().exec(externalProcess);

        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }
}
