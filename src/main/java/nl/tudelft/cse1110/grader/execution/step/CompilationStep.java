package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.execution.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.ResultBuilder;

import javax.tools.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        /**
         * creates the java compiler and diagnostic collector object
         * using just the standard configuration. Nothing to optimize here, I believe.
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager manager = compiler.getStandardFileManager(
                diagnostics, null, null );

        /**
         * Create a compilation task with the list of files to compile.
         * Also pass the classpath with the libraries, e.g., JUnit, JQWik, etc.
         */
        List<File> listOfFiles = cfg.getFilesToBeCompiled().stream()
                .map(filePath -> new File(filePath))
                .collect(Collectors.toList());
        Iterable<? extends JavaFileObject > sources =
                manager.getJavaFileObjectsFromFiles(listOfFiles);

        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, diagnostics,
                Arrays.asList(
                        new String[] { "-cp", cfg.getLibrariesClasspath()} /* classpath */
                ), null, sources);

        /**
         * Compiles. The .class files will be saved in the same directory
         * as the java files.
         */
        try {
            boolean compilationResult = task.call();

            if(compilationResult) {
                result.logSuccess(this);
                flow.next(new LoadGeneratedClassesStep());
            }
            else {
                result.compilationFail(diagnostics.getDiagnostics());
                flow.next(new GenerateResultsStep());
            }

            manager.close();
        } catch(Exception e) {
            result.genericFailure(e);
            flow.next(new GenerateResultsStep());
        }
    }
}
