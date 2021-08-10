package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.FromBytesClassLoader;
import nl.tudelft.cse1110.grader.result.GradeValues;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunMetaTests implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        int score = 0;

        try {
            /**Get the required files - meta classes and the solution*/
            List<File> metaClasses = FileUtils.getMetaFiles(dirCfg.getWorkingDir());
            String solutionFile = FileUtils.findSolution(dirCfg.getWorkingDir());
            List<String> failures = new ArrayList<>();

            for (File metaClass : metaClasses) {
                /**Save the current class loader and create a new one. Otherwise Java will not use the meta classes.*/
                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                ClassLoader classLoader = new FromBytesClassLoader();
                Thread.currentThread().setContextClassLoader(classLoader);

                /**Create a new directory to run the meta tests in.*/
                File tempDir = FileUtils.createTemporaryDirectory("metaWorkplace").toFile();

                /**Copy the solution file to the temporary directory.*/
                FileUtils.copyFile(solutionFile, tempDir.getAbsolutePath());

                /**Prepare an execution flow for the meta class.*/
                DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                        tempDir.toString(),
                        dirCfg.getLibrariesDir(),
                        dirCfg.getReportsDir()
                );

                Configuration metaCfg = new Configuration();
                metaCfg.setDirectoryConfiguration(metaDirCfg);

                ResultBuilder metaResult = new ResultBuilder();

                ExecutionFlow flow = ExecutionFlow.justTests(metaCfg, metaResult);

                /**Copy the meta class to the temporary directory and, since the meta classes are in .txt files,
                 * rename the meta class to a .java file.
                 */
                File newMetaFile = FileUtils.copyFile(metaClass.getAbsolutePath(), tempDir.getAbsolutePath()).toFile();
                newMetaFile.renameTo(new File(newMetaFile.getParentFile() + "/Library.java"));

                /**Run the flow for the meta class.*/
                flow.run();

                /**Check if some tests failed. If they did, we increase the score, otherwise we show which test failed.*/
                int testsRan = metaResult.getTestsRan();
                int testsSucceeded = metaResult.getTestsSucceeded();

                if (testsSucceeded < testsRan) {
                    score++;
                } else {
                    String metaName = metaClass.getName().replace(".txt", "");
                    failures.add(metaName);
                }

                /**Delete the temporary meta working directory after execution.*/
                FileUtils.deleteDirectory(tempDir);

                /**Restore the old class loader.*/
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }

            result.logMetaTests(score, metaClasses.size(), failures);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

}
