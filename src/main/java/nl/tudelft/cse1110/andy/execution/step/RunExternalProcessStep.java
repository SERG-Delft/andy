package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.execution.process.ExternalProcess;

public class RunExternalProcessStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            RunConfiguration runConfiguration = ctx.getRunConfiguration();
            DirectoryConfiguration directoryConfiguration = ctx.getDirectoryConfiguration();

            ExternalProcess externalProcess = runConfiguration.externalProcess();

            if (externalProcess == null) {
                return;
            }

            externalProcess.launch();

            ctx.setExternalProcess(externalProcess);

            externalProcess.await();

        } catch (Exception e) {
            result.genericFailure(this, e);
        }
    }
}
