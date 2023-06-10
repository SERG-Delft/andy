package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

public class CheckRunConfigurationWeightValidityStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        RunConfiguration runConfiguration = ctx.getRunConfiguration();
        
    }
}
