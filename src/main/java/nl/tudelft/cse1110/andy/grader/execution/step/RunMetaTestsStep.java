package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.config.MetaTest;
import nl.tudelft.cse1110.andy.grader.config.RunConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.Action;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.Mode;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.ModeSelector;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.util.FilesUtils.*;

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
            List<String> failures = new ArrayList<>();

            /*
             * For each meta test, we basically perform a string replace in the
             * original library code, recompile it, and run the tests.
             * If there's a failing test, the meta test is killed.
             *
             * We reuse our execution framework to run the code with the meta test.
             */
            for (MetaTest metaTest : metaTests) {
                result.debug(this, String.format("Preparing meta test %s", metaTest.getName()));

                /* Set the classloader to the cleanest classloader we have */
                Thread.currentThread().setContextClassLoader(ctx.getCleanClassloader());

                /* Copy the library and replace the library by the meta test */
                File metaWorkingDir = createTemporaryDirectory("metaWorkplace").toFile();
                copyFile(solutionFile, metaWorkingDir.getAbsolutePath());
                String metaFileContent = generateMetaFileContent(metaTest, originalLibraryContent);
                createMetaTestFile(metaWorkingDir, metaFileContent);

                /* We then run the meta test, using our infrastructure */
                ResultBuilder metaResult = runMetaTest(dirCfg, metaWorkingDir);

                /* And check the result. If there's a failing test, the test suite is good! */
                int testsRan = metaResult.getTestsRan();
                int testsSucceeded = metaResult.getTestsSucceeded();
                boolean passesTheMetaTest = testsSucceeded < testsRan;

                result.debug(this, metaResult.buildDebugResult());
                result.debug(this, String.format("Tests ran with the meta test: %d/%d", testsSucceeded, testsRan));

                if (passesTheMetaTest) {
                    score++;
                } else {
                    String metaName = metaTest.getName();
                    failures.add(metaName);
                }

                /* Clean up the directory */
                deleteDirectory(metaWorkingDir);
            }

            result.logMetaTests(score, metaTests.size(), failures);
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

    private ResultBuilder runMetaTest(DirectoryConfiguration dirCfg, File metaWorkingDir) {
        DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                metaWorkingDir.toString(),
                dirCfg.getOutputDir()
        );

        Context metaCtx = new Context(Action.TESTS);
        metaCtx.setDirectoryConfiguration(metaDirCfg);

        ModeSelector modeSelector = new ModeSelector(Mode.PRACTICE, Action.TESTS);

        ResultBuilder metaResult = new ResultBuilder();
        metaResult.setModeSelector(modeSelector);

        ExecutionFlow flow = ExecutionFlow.justTests(metaCtx, metaResult);
        flow.run();

        return metaResult;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunMetaTestsStep;
    }
}
