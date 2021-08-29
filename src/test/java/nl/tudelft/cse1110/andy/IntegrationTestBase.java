package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.andy.TestResourceUtils.resourceFolder;

public abstract class IntegrationTestBase {
    @TempDir
    protected Path reportDir;

    @TempDir
    protected Path workDir;

    public String run(Action action, List<ExecutionStep> plan,
                      String libraryFile, String solutionFile, String configurationFile,
                      ResultBuilder resultBuilder) {
        if (configurationFile != null) {
            copyConfigurationFile(configurationFile);
        }

        copyFiles(libraryFile, solutionFile);

        Context ctx = new Context(action);

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                reportDir.toString()
        );

        ctx.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = resultBuilder;

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            ExecutionFlow flow = ExecutionFlow.asSteps(plan, ctx, result);
            flow.run();
        } catch(Exception e) {
            // something went wrong, let's print the debugging version in the console
            // and fail the test
            System.out.println(result.buildDebugResult());
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }

        return readStdOut();
    }

    public String run(List<ExecutionStep> plan, String libraryFile, String solutionFile) {
        return this.run(Action.CUSTOM, plan, libraryFile, solutionFile, null, new ResultBuilder());
    }

    public String run(List<ExecutionStep> plan, String libraryFile, String solutionFile, String configurationFile) {
        return this.run(Action.CUSTOM, plan, libraryFile, solutionFile, configurationFile, new ResultBuilder());
    }

    public String run(Action action, List<ExecutionStep> plan, String libraryFile, String solutionFile, String configurationFile) {
        return this.run(action, plan, libraryFile, solutionFile, configurationFile, new ResultBuilder());
    }

    public String run(List<ExecutionStep> plan,
                      String libraryFile, String solutionFile, String configurationFile,
                      ResultBuilder resultBuilder) {
        return this.run(Action.CUSTOM, plan, libraryFile, solutionFile, configurationFile, resultBuilder);
    }

    private String readStdOut() {
        return FilesUtils.readFile(new File(FilesUtils.concatenateDirectories(workDir.toString(), "stdout.txt")));
    }


    protected void copyFiles(String libraryFile, String solutionFile) {
        String dirWithLibrary = resourceFolder("/grader/fixtures/Library/");
        String dirWithSolution = resourceFolder("/grader/fixtures/Solution/");
        File library = new File(dirWithLibrary +  libraryFile + ".java");
        File solution = new File(dirWithSolution + solutionFile + ".java");

        String dirToCopy = workDir.toString();

        File copiedLibrary = FilesUtils.copyFile(library.getAbsolutePath(), dirToCopy).toFile();
        File copiedSolution = FilesUtils.copyFile(solution.getAbsolutePath(), dirToCopy).toFile();

        copiedLibrary.renameTo(new File(copiedLibrary.getParentFile() + "/Library.java"));
        copiedSolution.renameTo(new File(copiedSolution.getParentFile() + "/Solution.java"));
    }

    protected void copyConfigurationFile(String configurationFile) {
        String dirWithConfiguration = resourceFolder("/grader/fixtures/Config/");

        File config = new File(dirWithConfiguration + configurationFile + ".java");
        File copied = FilesUtils.copyFile(config.getAbsolutePath(), workDir.toString()).toFile();

        copied.renameTo(new File(copied.getParentFile() + "/Configuration.java"));
    }

}
