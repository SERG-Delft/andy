package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
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

            ctx.setRunConfiguration(runConfiguration);
        } catch (NoSuchElementException ex) {
            // There's no configuration set. We put a default one!
            RunConfiguration runConfiguration = new DefaultRunConfiguration(allClassesButTestingAndConfigOnes(ctx.getNewClassNames()));
            ctx.setRunConfiguration(runConfiguration);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GetRunConfigurationStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
