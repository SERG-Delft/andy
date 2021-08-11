package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.GradeValues;
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

            this.setGradeValues(runConfiguration, result);
        } catch (NoSuchElementException ex) {
            RunConfiguration runConfiguration = new DefaultRunConfiguration(cfg.getDirectoryConfiguration());
            cfg.setRunConfiguration(runConfiguration);

            this.setGradeValues(runConfiguration, result);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    private void setGradeValues(RunConfiguration runCfg, ResultBuilder result) {
        boolean failureGivesZero = runCfg.failureGivesZero();
        float coverage = runCfg.weights().get("coverage");
        float mutation = runCfg.weights().get("mutation");
        float meta = runCfg.weights().get("meta");
        float codechecks = runCfg.weights().get("codechecks");

        result.setGradeValues(new GradeValues(failureGivesZero, coverage, mutation, meta, codechecks));
    }
}
