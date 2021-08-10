package nl.tudelft.cse1110.grader.integration;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.GradeValues;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.tudelft.cse1110.ResourceUtils.permanentResourceFolder;
import static nl.tudelft.cse1110.ResourceUtils.resourceFolder;
import static org.apache.commons.io.FileUtils.copyURLToFile;

public abstract class GraderIntegrationTestBase {


    @TempDir
    protected Path reportDir;

    @TempDir
    protected Path workDir;


    // the URL list of the required libraries
    static Set<String> requiredLibraries = new HashSet<>() {{
        // assertj
        add("https://repo1.maven.org/maven2/org/assertj/assertj-core/3.20.2/assertj-core-3.20.2.jar");

        // junit
        add("https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.8.0-M1/junit-jupiter-api-5.8.0-M1.jar");
        add("https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-params/5.8.0-M1/junit-jupiter-params-5.8.0-M1.jar");
        add("https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.8.0-M1/junit-jupiter-engine-5.8.0-M1.jar");

        // mockito
        add("https://repo1.maven.org/maven2/org/mockito/mockito-core/3.11.2/mockito-core-3.11.2.jar");

        // jqwik
        add("https://repo1.maven.org/maven2/net/jqwik/jqwik/1.5.3/jqwik-1.5.3.jar");
        add("https://repo1.maven.org/maven2/net/jqwik/jqwik-api/1.5.3/jqwik-api-1.5.3.jar");
        add("https://repo1.maven.org/maven2/net/jqwik/jqwik-engine/1.5.3/jqwik-engine-1.5.3.jar");
    }};


    private static void downloadLibsIfNeeded(String libDirectory) {
        try {
            // if the lib folder doesn't contain the expected libs, we download them
            if(Files.list(Paths.get(libDirectory)).count() == 1) {

                for (String requiredLibrary : requiredLibraries) {
                    String jarName = requiredLibrary.substring(requiredLibrary.lastIndexOf('/') + 1);
                    copyURLToFile(new URL(requiredLibrary), new File(libDirectory, jarName));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String run(List<ExecutionStep> plan, CheckScript codeCheckerScript, String libraryFile, String solutionFile) {
        copyFiles(libraryFile, solutionFile);

        Configuration cfg = new Configuration();

        DirectoryConfiguration dirCfg = new DirectoryConfiguration(
                workDir.toString(),
                getLibDirectory(),
                reportDir.toString(),
                codeCheckerScript
        );

        cfg.setDirectoryConfiguration(dirCfg);

        ResultBuilder result = new ResultBuilder();

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

        ExecutionFlow flow = ExecutionFlow.asSteps(plan, cfg, result);
        flow.run();

        Thread.currentThread().setContextClassLoader(oldClassLoader);

        return result.buildEndUserResult();
    }

    public String run(List<ExecutionStep> plan, CheckScript codeCheckerScript, String libraryFile, String solutionFile, String metaDirectory) {
        this.copyMetaFiles(metaDirectory);

        return this.run(plan, codeCheckerScript, libraryFile, solutionFile);
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


    private static String getLibDirectory() {
        String libPath = permanentResourceFolder();
        downloadLibsIfNeeded(libPath);
        return libPath;
    }

    private void copyMetaFiles(String metaDirectory) {
        String dirWithMeta = resourceFolder("/grader/fixtures/Meta/");

        File meta = new File(dirWithMeta + metaDirectory);
        for (File file : meta.listFiles()) {
            try {
                FileUtils.copyFile(file.getAbsolutePath(), workDir.toString());
            } catch (Exception ex) {}
        }
    }


}
