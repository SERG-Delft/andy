package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.grade.GradeWeight;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.util.NoSuchElementException;

import static nl.tudelft.cse1110.grader.util.ClassUtils.getConfigurationClass;

public class GetRunConfigurationStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            Class<?> runConfigurationClass = Class.forName(getConfigurationClass(dirCfg.getNewClassNames()), false, classLoader);
            RunConfiguration runConfiguration = (RunConfiguration) runConfigurationClass.getDeclaredConstructor().newInstance();

            cfg.setRunConfiguration(runConfiguration);

            this.buildGradeValues(runConfiguration, result);
        } catch (NoSuchElementException ex) {
            // There's no configuration set. We put a default one!
            RunConfiguration runConfiguration = new DefaultRunConfiguration(cfg.getDirectoryConfiguration());
            cfg.setRunConfiguration(runConfiguration);

            this.buildGradeValues(runConfiguration, result);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    private void buildGradeValues(RunConfiguration runCfg, ResultBuilder result) {
        boolean failureGivesZero = runCfg.failureGivesZero();
        float coverage = runCfg.weights().get("coverage");
        float mutation = runCfg.weights().get("mutation");
        float meta = runCfg.weights().get("meta");
        float codechecks = runCfg.weights().get("codechecks");

        result.setGradeWeights(new GradeWeight(failureGivesZero, coverage, mutation, meta, codechecks));
    }
}
