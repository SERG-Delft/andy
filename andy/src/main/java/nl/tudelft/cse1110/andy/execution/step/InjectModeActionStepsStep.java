package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

public class InjectModeActionStepsStep implements ExecutionStep {
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        ExecutionFlow flow = ctx.getFlow();
        ModeActionSelector modeActionSelector = createModeSelector(ctx, result);
        flow.addSteps(modeActionSelector.getSteps());
    }

    private ModeActionSelector createModeSelector(Context ctx, ResultBuilder result) {
        RunConfiguration runConfiguration = ctx.getRunConfiguration();

        Mode mode = runConfiguration.mode();
        Action action = ctx.getAction();

        ModeActionSelector modeActionSelector = new ModeActionSelector(mode, action);
        ctx.setModeSelector(modeActionSelector);

        return modeActionSelector;
    }
}
