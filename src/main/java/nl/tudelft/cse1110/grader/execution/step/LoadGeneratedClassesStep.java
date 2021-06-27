package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

/**
 * This step loads the newly compiled classes into the JVM.
 * It does that by copying the new .class files into a folder that's been currently
 * monitored by the JVM and load them. It then tries to load them just to ensure
 * things are ok.
 *
 * Pre-condition: It assumes the compilation step was executed
 */
public class LoadGeneratedClassesStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        try {
            for(String className : cfg.getNewClassNames()) {
                Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            result.genericFailure(this, e);
            flow.next(new GenerateResultsStep());
            return;
        }

        flow.next(new RunJUnitTests());
    }

}
