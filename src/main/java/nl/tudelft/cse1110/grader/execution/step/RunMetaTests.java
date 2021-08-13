package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.FromBytesClassLoader;
import nl.tudelft.cse1110.grader.execution.MetaTest;
import nl.tudelft.cse1110.grader.result.GradeValues;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunMetaTests implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        RunConfiguration runCfg = cfg.getRunConfiguration();

        int score = 0;

        try {
            /**Get the required files - meta classes and the solution*/
            List<MetaTest> metaTests = runCfg.metaTests();
            String solutionFile = FileUtils.findSolution(dirCfg.getWorkingDir());
            List<String> failures = new ArrayList<>();

            for (MetaTest metaTest : metaTests) {
                /**Save the current class loader and create a new one. Otherwise Java will not use the meta classes.*/
                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                ClassLoader classLoader = new FromBytesClassLoader();
                Thread.currentThread().setContextClassLoader(classLoader);

                /**Create a new directory to run the meta tests in.*/
                File tempWorkingDir = FileUtils.createTemporaryDirectory("metaWorkplace").toFile();

                /**Copy the solution file to the temporary directory.*/
                FileUtils.copyFile(solutionFile, tempWorkingDir.getAbsolutePath());

                /**Create the meta file in the temporary directory and put the content corresponding to this meta test.*/
                File libraryFile = new File(FileUtils.findLibrary(dirCfg.getWorkingDir()));
                String originalLibraryContent = FileUtils.readFile(libraryFile);
                String metaFileContent = metaTest.evaluate(originalLibraryContent);

                if (metaFileContent.equals(originalLibraryContent)) {
                    throw new RuntimeException("Meta test " + metaTest.getName() + " failed to replace code.");
                }

                File metaFile = new File(tempWorkingDir + "/Library.java");
                if (!metaFile.createNewFile()) {
                    throw new RuntimeException("Could not create a library file for meta testing.");
                }
                FileUtils.writeToFile(metaFile, metaFileContent);

                /**Prepare an execution flow for the meta class.*/
                DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                        tempWorkingDir.toString(),
                        dirCfg.getLibrariesDir(),
                        dirCfg.getReportsDir()
                );

                Configuration metaCfg = new Configuration();
                metaCfg.setDirectoryConfiguration(metaDirCfg);

                ResultBuilder metaResult = new ResultBuilder();

                ExecutionFlow flow = ExecutionFlow.justTests(metaCfg, metaResult);

                /**Run the flow for the meta class.*/
                flow.run();

                /**Check if some tests failed. If they did, we increase the score, otherwise we show which test failed.*/
                int testsRan = metaResult.getTestsRan();
                int testsSucceeded = metaResult.getTestsSucceeded();

                if (testsSucceeded < testsRan) {
                    score++;
                } else {
                    String metaName = metaTest.getName();
                    failures.add(metaName);
                }

                /**Delete the temporary meta working directory after execution.*/
                FileUtils.deleteDirectory(tempWorkingDir);

                /**Restore the old class loader.*/
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }

            result.logMetaTests(score, metaTests.size(), failures);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

}
