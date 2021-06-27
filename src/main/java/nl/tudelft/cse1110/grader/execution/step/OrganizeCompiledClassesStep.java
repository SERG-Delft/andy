package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import static nl.tudelft.cse1110.grader.util.ClassUtils.extractClassName;
import static nl.tudelft.cse1110.grader.util.ClassUtils.extractDirectoryName;
import static nl.tudelft.cse1110.grader.util.FileUtils.*;

/**
 * This step creates the folders as expected by the JVM
 * e.g., if a class is called a.b.c.D, then it needs to be
 * in a/b/c/D.class.
 */
public class OrganizeCompiledClassesStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {

        try {
            for (String newClassName : cfg.getNewClassNames()) {
                String directoryName = concatenateDirectories(cfg.getSourceCodeDir(), extractDirectoryName(newClassName));
                String className = extractClassName(newClassName);

                createDirIfNeeded(directoryName);
                moveClass(cfg.getSourceCodeDir(), className, directoryName);
            }

            flow.next(new LoadGeneratedClassesStep());
        } catch(Exception e) {
            result.genericFailure(this, e);
            flow.next(new GenerateResultsStep());
        }
    }



}
