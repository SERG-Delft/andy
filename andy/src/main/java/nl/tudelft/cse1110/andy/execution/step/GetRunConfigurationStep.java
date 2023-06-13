package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.util.NoSuchElementException;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.allClassesButTestingAndConfigOnes;
import static nl.tudelft.cse1110.andy.utils.ClassUtils.getConfigurationClass;

public class GetRunConfigurationStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

            Class<?> runConfigurationClass = Class.forName(getConfigurationClass(ctx.getNewClassNames()), false, currentClassLoader);
            RunConfiguration runConfiguration = (RunConfiguration) runConfigurationClass.getDeclaredConstructor().newInstance();

            if(!isValidRunConfiguration(runConfiguration)) {
                // load default configuration
                ctx.setRunConfiguration(new DefaultRunConfiguration(allClassesButTestingAndConfigOnes(ctx.getNewClassNames())));
                throw new RuntimeException("The run configuration has invalid weights. Please check the file.");
            }

            ctx.setRunConfiguration(runConfiguration);
        } catch (NoSuchElementException ex) {
            // load default configuration
            ctx.setRunConfiguration(new DefaultRunConfiguration(allClassesButTestingAndConfigOnes(ctx.getNewClassNames())));
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    /**
     * Checks whether the config is valid.
     * - Sum of weights must be 1.0
     * - If Jacoco / Pitest are skipped, the weight should be 0
     * @param config The run configuration of the problem
     * @return validity of the config weights represented as a boolean
     */
    public static boolean isValidRunConfiguration(RunConfiguration config) {
        var weights = config.weights();
        float coverage = weights.get("coverage");
        float mutation = weights.get("mutation");
        float meta = weights.get("meta");
        float codechecks = weights.get("codechecks");
        if(coverage + mutation + meta + codechecks != 1.0)
            return false;
        if(config.skipPitest() && mutation != 0.0)
            return false;
        if(config.skipJacoco() && coverage != 0.0)
            return false;
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GetRunConfigurationStep;
    }
}
