package nl.tudelft.cse1110;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static nl.tudelft.cse1110.ResourceUtils.resourceFolder;
import static nl.tudelft.cse1110.grader.util.FileUtils.concatenateDirectories;

public abstract class IntegrationTestBase {
    @TempDir
    protected Path reportDir;     

    @TempDir
    protected Path workDir;

    public String run(List<ExecutionStep> plan, String libraryFile, String solutionFile) {
        copyFiles(libraryFile, solutionFile);

        Configuration cfg = new Configuration();

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                reportDir.toString()
        );

        cfg.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = new ResultBuilder();

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        ExecutionFlow flow = ExecutionFlow.asSteps(plan, cfg, result);
        flow.run();

        Thread.currentThread().setContextClassLoader(oldClassLoader);

        return readStdOut();
    }

    private String readStdOut() {
        return FileUtils.readFile(new File(concatenateDirectories(workDir.toString(), "stdout.txt")));
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

        File copiedLibrary = FileUtils.copyFile(library.getAbsolutePath(), dirToCopy).toFile();
        File copiedSolution = FileUtils.copyFile(solution.getAbsolutePath(), dirToCopy).toFile();

        copiedLibrary.renameTo(new File(copiedLibrary.getParentFile() + "/Library.java"));
        copiedSolution.renameTo(new File(copiedSolution.getParentFile() + "/Solution.java"));
    }

    protected void copyConfigurationFile(String configurationFile) {
        String dirWithConfiguration = resourceFolder("/grader/fixtures/Config/");

        File config = new File(dirWithConfiguration + configurationFile + ".java");
        File copied = FileUtils.copyFile(config.getAbsolutePath(), workDir.toString()).toFile();

        copied.renameTo(new File(copied.getParentFile() + "/Configuration.java"));
    }

}
