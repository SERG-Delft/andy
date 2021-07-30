package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DefaultConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.FromBytesClassLoader;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RunMetaTests implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        if (cfg.getMetaDir() == null) {
            return;
        }

        int score = 0;

        try {
            List<File> metaClasses = FileUtils.getMetaFiles(cfg.getMetaDir());
            String solutionFile = FileUtils.findSolution(cfg.getWorkingDir());
            List<String> failures = new ArrayList<>();

            for (File metaClass : metaClasses) {
                ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
                ClassLoader classLoader = new FromBytesClassLoader();
                Thread.currentThread().setContextClassLoader(classLoader);

                File tempDir = FileUtils.createTemporaryDirectory("metaWorkplace").toFile();

                FileUtils.copyFile(solutionFile, tempDir.getAbsolutePath());

                Configuration metaCfg = new DefaultConfiguration(
                        cfg.getMainLibraryClass(),
                        tempDir.toString(),
                        cfg.getLibrariesDir(),
                        cfg.getReportsDir(),
                        cfg.getCodeCheckerScript()
                );

                ResultBuilder metaResult = new ResultBuilder();

                ExecutionFlow flow = ExecutionFlow.justTests(metaCfg, metaResult);

                File newMetaFile = FileUtils.copyFile(metaClass.getAbsolutePath(), tempDir.getAbsolutePath()).toFile();
                newMetaFile.renameTo(new File(newMetaFile.getParentFile() + "/Library.java"));

                flow.run();

                int testsRan = metaResult.getTestsRan();
                int testsSucceeded = metaResult.getTestsSucceeded();

                if (testsSucceeded < testsRan) {
                    score++;
                } else {
                    failures.add(metaClass.getName().replace(".java", ""));
                }

                FileUtils.deleteDirectory(tempDir);

                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }

            result.logMetaTests(score, metaClasses.size(), failures);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

}
