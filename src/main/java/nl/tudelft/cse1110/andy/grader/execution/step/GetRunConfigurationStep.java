package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.grader.config.RunConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.mode.Action;
import nl.tudelft.cse1110.andy.grader.execution.mode.Mode;
import nl.tudelft.cse1110.andy.grader.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import java.util.NoSuchElementException;

import static nl.tudelft.cse1110.andy.grader.util.ClassUtils.allClassesButTestingAndConfigOnes;
import static nl.tudelft.cse1110.andy.grader.util.ClassUtils.getConfigurationClass;

public class GetRunConfigurationStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

            Class<?> runConfigurationClass = Class.forName(getConfigurationClass(ctx.getNewClassNames()), false, currentClassLoader);
            RunConfiguration runConfiguration = (RunConfiguration) runConfigurationClass.getDeclaredConstructor().newInstance();

            ctx.setRunConfiguration(runConfiguration);
            this.addExecutionSteps(ctx, result);
        } catch (NoSuchElementException ex) {
            // There's no configuration set. We put a default one!
            RunConfiguration runConfiguration = new DefaultRunConfiguration(allClassesButTestingAndConfigOnes(ctx.getNewClassNames()));
            ctx.setRunConfiguration(runConfiguration);
            this.addExecutionSteps(ctx, result);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }


    private void addExecutionSteps(Context ctx, ResultBuilder result) {
        ExecutionFlow flow = ctx.getFlow();

        ModeActionSelector modeActionSelector = createModeSelector(ctx, result);

        // Custom action means the steps of the flow have already been declared.
        if (modeActionSelector.getAction() == Action.CUSTOM) {
            return;
        }

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

    @Override
    public boolean equals(Object other) {
        return other instanceof GetRunConfigurationStep;
    }
}
