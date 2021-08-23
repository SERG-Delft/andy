package nl.tudelft.cse1110.andy;

import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;
import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.andy.ResourceUtils.resourceFolder;
import static org.mockito.Mockito.*;

public abstract class IntegrationTestBase {
    @TempDir
    protected Path reportDir;     

    @TempDir
    protected Path workDir;

    public String run(List<ExecutionStep> plan, String libraryFile, String solutionFile) {
        copyFiles(libraryFile, solutionFile);

        Context cfg = new Context("HINTS");

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                reportDir.toString()
        );

        cfg.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = new ResultBuilder();

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            ExecutionFlow flow = ExecutionFlow.asSteps(plan, cfg, result);
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

    private String readStdOut() {
        return FilesUtils.readFile(new File(FilesUtils.concatenateDirectories(workDir.toString(), "stdout.txt")));
    }

    public String run(List<ExecutionStep> plan, String libraryFile, String solutionFile, String configurationFile) {
        this.copyConfigurationFile(configurationFile);

        return this.run(plan, libraryFile, solutionFile);
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
