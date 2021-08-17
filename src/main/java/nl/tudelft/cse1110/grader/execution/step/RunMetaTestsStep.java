package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.MetaTest;
import nl.tudelft.cse1110.grader.execution.step.helper.FromBytesClassLoader;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RunMetaTestsStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        RunConfiguration runCfg = cfg.getRunConfiguration();

        int score = 0;

        try {
            List<MetaTest> metaTests = runCfg.metaTests();
            String solutionFile = FileUtils.findSolution(dirCfg.getWorkingDir());
            List<String> failures = new ArrayList<>();

            for (MetaTest metaTest : metaTests) {
                ClassLoader oldClassLoader = changeClassLoader();

                File metaWorkingDir = FileUtils.createTemporaryDirectory("metaWorkplace").toFile();

                FileUtils.copyFile(solutionFile, metaWorkingDir.getAbsolutePath());

                String metaFileContent = generateMetaFileContent(metaTest, dirCfg);

                createMetaTestFile(metaWorkingDir, metaFileContent);

                ResultBuilder metaResult = runMetaTest(dirCfg, metaWorkingDir);

                int testsRan = metaResult.getTestsRan();
                int testsSucceeded = metaResult.getTestsSucceeded();

                if (testsSucceeded < testsRan) {
                    score++;
                } else {
                    String metaName = metaTest.getName();
                    failures.add(metaName);
                }

                FileUtils.deleteDirectory(metaWorkingDir);

                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }

            result.logMetaTests(score, metaTests.size(), failures);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    private ClassLoader changeClassLoader() {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = new FromBytesClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        return oldClassLoader;
    }

    private String generateMetaFileContent(MetaTest metaTest, DirectoryConfiguration dirCfg) {
        File libraryFile = new File(FileUtils.findLibrary(dirCfg.getWorkingDir()));
        String originalLibraryContent = FileUtils.readFile(libraryFile);
        String metaFileContent = metaTest.evaluate(originalLibraryContent);

        if (metaFileContent.equals(originalLibraryContent)) {
            throw new RuntimeException("Meta test " + metaTest.getName() + " failed to replace code.");
        }

        return metaFileContent;
    }

    private void createMetaTestFile(File metaWorkingDir, String metaFileContent) throws Exception {
        File metaFile = new File(metaWorkingDir + "/Library.java");
        if (!metaFile.createNewFile()) {
            throw new IOException("Failed to create a meta file.");
        }
        FileUtils.writeToFile(metaFile, metaFileContent);
    }

    private ResultBuilder runMetaTest(DirectoryConfiguration dirCfg, File metaWorkingDir) {
        DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                metaWorkingDir.toString(),
                dirCfg.getOutputDir()
        );

        Configuration metaCfg = new Configuration();
        metaCfg.setDirectoryConfiguration(metaDirCfg);

        ResultBuilder metaResult = new ResultBuilder();

        ExecutionFlow flow = ExecutionFlow.justTests(metaCfg, metaResult);

        flow.run();

        return metaResult;
    }
}
