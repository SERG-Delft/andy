package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * This step replaces the classloader with one that sees the folder
 * with the compiled classes.
 */
public class ReplaceClassloaderStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        try {
            String pathToAddToClassloader = cfg.getSourceCodeDir();
            replaceClassloader(pathToAddToClassloader);

            flow.next(new RunJUnitTests());
        } catch (Exception e) {
            result.genericFailure(this, e);
            flow.next(new GenerateResultsStep());
        }
    }

    private void replaceClassloader(String pathToAddToClassloader) {
        List<Path> additionalClasspathEntries = Arrays.asList(Paths.get(pathToAddToClassloader));
        URL[] urls = additionalClasspathEntries.stream().map(this::toURL).toArray(URL[]::new);
        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader customClassLoader = URLClassLoader.newInstance(urls, parentClassLoader);

        Thread.currentThread().setContextClassLoader(customClassLoader);
    }

    private URL toURL(Path path) {
        try {
            return path.toUri().toURL();
        }
        catch (Exception ex) {
            throw new RuntimeException("Invalid classpath entry: " + path, ex);
        }
    }


}
