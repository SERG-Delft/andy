package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.ClassUtils;

import java.util.NoSuchElementException;

public class GetRunConfigurationStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            Class<?> runConfigurationClass = Class.forName(ClassUtils.getConfigurationClass(dirCfg.getNewClassNames()), false, classLoader);
            RunConfiguration runConfiguration = (RunConfiguration) runConfigurationClass.getDeclaredConstructor().newInstance();

            cfg.setRunConfiguration(runConfiguration);
        } catch (NoSuchElementException ex) {
           cfg.setRunConfiguration(new DefaultRunConfiguration(cfg.getDirectoryConfiguration()));
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }

    }
}
