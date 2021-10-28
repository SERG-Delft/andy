package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.MetaTestResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.writer.EmptyWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class RunMetaTestsStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        int score = 0;

        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        try {
            /* Get the student solution, which we will run for each meta test */
            String solutionFile = findSolution(dirCfg.getWorkingDir());

            /* Get the original library content that we will keep changing according to the meta test */
            File libraryFile = new File(findLibrary(dirCfg.getWorkingDir()));
            String originalLibraryContent = readFile(libraryFile);

            List<MetaTest> metaTests = runCfg.metaTests();
            List<MetaTestResult> metaTestResults = new ArrayList<>();

            /*
             * For each meta test, we basically perform a string replace in the
             * original library code, recompile it, and run the tests.
             * If there's a failing test, the meta test is killed.
             *
             * We reuse our execution framework to run the code with the meta test.
             */
            for (MetaTest metaTest : metaTests) {
                /* Set the classloader to the cleanest classloader we have */
                Thread.currentThread().setContextClassLoader(ctx.getCleanClassloader());

                /* Copy the library and replace the library by the meta test */
                File metaWorkingDir = createTemporaryDirectory("metaWorkplace").toFile();
                copyFile(solutionFile, metaWorkingDir.getAbsolutePath());
                String metaFileContent = generateMetaFileContent(metaTest, originalLibraryContent);
                createMetaTestFile(metaWorkingDir, metaFileContent);

                /* We then run the meta test, using our infrastructure */
                ResultBuilder metaResultBuilder = runMetaTest(ctx, dirCfg, metaWorkingDir);
                Result metaResult = metaResultBuilder.build();

                /* And check the result. If there's a failing test, the test suite is good! */
                int testsRan = metaResult.getTests().getTestsRan();
                int testsSucceeded = metaResult.getTests().getTestsSucceeded();
                boolean passesTheMetaTest = testsSucceeded < testsRan;

                if (passesTheMetaTest) {
                    score+= metaTest.getWeight();
                    metaTestResults.add(new MetaTestResult(metaTest.getName(), metaTest.getWeight(), true));
                } else {
                    metaTestResults.add(new MetaTestResult(metaTest.getName(), metaTest.getWeight(), false));
                }

                /* Clean up the directory */
                deleteDirectory(metaWorkingDir);
            }

            int totalWeight = metaTests.stream().mapToInt(m -> m.getWeight()).sum();
            result.logMetaTests(score, totalWeight, metaTestResults);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        } finally {
            /* restore the class loader to the one before meta tests */
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    private String generateMetaFileContent(MetaTest metaTest, String originalLibraryContent) {
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
        FilesUtils.writeToFile(metaFile, metaFileContent);
    }

    private ResultBuilder runMetaTest(Context ctx, DirectoryConfiguration dirCfg, File metaWorkingDir) {
        DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                metaWorkingDir.toString(),
                dirCfg.getOutputDir()
        );

        Context metaCtx = new Context(Action.META_TEST);
        metaCtx.setDirectoryConfiguration(metaDirCfg);
        metaCtx.setLibrariesToBeIncluded(ctx.getLibrariesToBeIncluded());

        ResultBuilder metaResult = new ResultBuilder(metaCtx, new GradeCalculator());

        ExecutionFlow flow = ctx.isSecurityEnabled()
                           ? ExecutionFlow.build(metaCtx, metaResult, new EmptyWriter())
                           : ExecutionFlow.buildWithoutSecurityManager(metaCtx, metaResult, new EmptyWriter());

        flow.run();

        return metaResult;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunMetaTestsStep;
    }
}
