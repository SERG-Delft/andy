package integration;

import com.google.common.io.Files;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.*;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.EmptyWriter;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.ResourceUtils.resourceFolder;

public abstract class IntegrationTestBase {

    protected final File workDir   = Files.createTempDir();
    protected final File reportDir = Files.createTempDir();
    protected Context ctx;

    @AfterEach
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(workDir);
        FileUtils.deleteDirectory(reportDir);
    }

    public Result run(Action action,
                      String libraryFile,
                      String solutionFile,
                      String configurationFile,
                      List<ExecutionStep> executionStepsOverride) {
        if (configurationFile != null) {
            copyConfigurationFile(configurationFile);
        }

        copyFiles(libraryFile, solutionFile);

        ContextDirector director = new ContextDirector(new ContextBuilder());
        this.ctx = director.constructBase(
                action,
                new DirectoryConfiguration(workDir.toString(), reportDir.toString())
        );

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            ExecutionFlow flow = executionStepsOverride == null ?
                    ExecutionFlow.build(ctx) :
                    ExecutionFlow.build(ctx, executionStepsOverride);

            addSteps(flow);

            Result result = flow.run();
            ResultWriter writer = getWriter();
            writer.write(ctx, result);

            return result;
        } catch(Exception e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    public Result run(Action action,
                      String libraryFile,
                      String solutionFile,
                      String configurationFile) {
        return run(action, libraryFile, solutionFile, configurationFile, null);
    }

    protected ResultWriter getWriter() {
        return new EmptyWriter();
    }

    public Result run(Action action, String libraryFile, String solutionFile,
                      List<ExecutionStep> executionStepsOverride) {
        return this.run(action, libraryFile, solutionFile, null, executionStepsOverride);
    }

    public Result run(String libraryFile, String solutionFile, String configurationFile) {
        return this.run(Action.FULL_WITH_HINTS, libraryFile, solutionFile, configurationFile);
    }

    public Result run(Action action, String libraryFile, String solutionFile) {
        return this.run(action, libraryFile, solutionFile, (String) null);
    }

    protected void addSteps(ExecutionFlow flow) {
    }

    private static boolean containsAbsolutePath(String file) {
        String fileSeparator = System.getProperty("file.separator");
        return file.contains(fileSeparator);
    }

    protected void copyFiles(String libraryFile, String solutionFile) {
        String dirWithLibrary = resourceFolder("/grader/fixtures/Library/");
        String dirWithSolution = resourceFolder("/grader/fixtures/Solution/");

        File library = containsAbsolutePath(libraryFile) ?
                new File(libraryFile) :
                new File(dirWithLibrary +  libraryFile + ".java");

        File solution = containsAbsolutePath(solutionFile) ?
                new File(solutionFile) :
                new File(dirWithSolution + solutionFile + ".java");

        String dirToCopy = workDir.toString();

        File copiedLibrary = FilesUtils.copyFile(library.getAbsolutePath(), dirToCopy).toFile();
        File copiedSolution = FilesUtils.copyFile(solution.getAbsolutePath(), dirToCopy).toFile();

        copiedLibrary.renameTo(new File(copiedLibrary.getParentFile() + "/Library.java"));
        copiedSolution.renameTo(new File(copiedSolution.getParentFile() + "/Solution.java"));
    }

    protected void copyConfigurationFile(String configurationFile) {
        File config;

        config = containsAbsolutePath(configurationFile) ?
                new File(configurationFile) :
                new File(resourceFolder("/grader/fixtures/Config/") + configurationFile + ".java");

        File copied = FilesUtils.copyFile(config.getAbsolutePath(), workDir.toString()).toFile();

        copied.renameTo(new File(copied.getParentFile() + "/Configuration.java"));
    }

}
