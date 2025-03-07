package nl.tudelft.cse1110.andy.execution.metatest.library;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.Context.ContextBuilder;
import nl.tudelft.cse1110.andy.execution.Context.ContextDirector;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.metatest.AbstractMetaTest;
import nl.tudelft.cse1110.andy.execution.metatest.library.evaluators.MetaEvaluator;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.CompilationErrorInfo;
import nl.tudelft.cse1110.andy.result.CompilationResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.utils.CodeSnippetUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.*;

public class LibraryMetaTest extends AbstractMetaTest {
    private MetaEvaluator evaluator;

    public LibraryMetaTest(int weight, String name, MetaEvaluator evaluator) {
        super(weight, name);
        this.evaluator = evaluator;
    }

    public String evaluate(String oldLibraryCode) {
        return this.evaluator.evaluate(oldLibraryCode);
    }

    @Override
    public boolean execute(Context ctx, DirectoryConfiguration dirCfg, RunConfiguration runCfg) throws Exception {
        /* Get the student solution, which we will run for each meta test */
        String solutionFile = findSolution(dirCfg.getWorkingDir());

        /* Get the original library content that we will keep changing according to the meta test */
        File libraryFile = new File(findLibrary(dirCfg.getWorkingDir()));
        String originalLibraryContent = readFile(libraryFile);

        /*
         * For each meta test, we basically perform a string replace in the
         * original library code, recompile it, and run the tests.
         * If there's a failing test, the meta test is killed.
         *
         * We reuse our execution framework to run the code with the meta test.
         */
        boolean passesTheMetaTest;

        /* Copy the library and replace the library by the meta test */
        File metaWorkingDir = createTemporaryDirectory("metaWorkplace").toFile();
        try {
            copyFile(solutionFile, metaWorkingDir.getAbsolutePath());
            String metaFileContent = generateMetaFileContent(originalLibraryContent);
            createMetaTestFile(metaWorkingDir, metaFileContent);

            /* We then run the meta test, using our infrastructure */
            Result metaResult = runMetaTest(ctx, dirCfg, metaWorkingDir);

            verifyMetaTestExecution(metaResult, metaFileContent);

            /* And check the result. If there's a failing test, the test suite is good! */
            int testsRan = metaResult.getTests().getTestsRan();
            int testsSucceeded = metaResult.getTests().getTestsSucceeded();
            passesTheMetaTest = testsSucceeded < testsRan;
        } finally {
            /* Clean up the directory */
            deleteDirectory(metaWorkingDir);

            /* Set the classloader back to the one with the student's original code */
            Thread.currentThread().setContextClassLoader(ctx.getClassloaderWithStudentsCode());
        }
        return passesTheMetaTest;
    }

    private void verifyMetaTestExecution(Result metaResult, String metaFileContent) {
        if (!metaResult.getCompilation().wasExecuted() || !metaResult.getCompilation().successful()) {
            String msg = generateCompilationErrorMessage(metaResult.getCompilation(), metaFileContent);
            throw new RuntimeException(msg);
        }

        if (metaResult.hasGenericFailure()) {
            throw new RuntimeException("Meta test '" + this.getName() + "' failed to run: " + metaResult.getGenericFailure().toString());
        }

        if (metaResult.getTests().getTestsRan() == 0) {
            throw new RuntimeException("Meta test '" + this.getName() + "' failed to run any tests");
        }
    }

    private String generateCompilationErrorMessage(CompilationResult compilationResult, String metaFileContent) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Meta test '").append(this.getName()).append("' failed to compile: ").append("\n");
        List<CompilationErrorInfo> errors = compilationResult.getErrors();
        for (int i = 0; i < errors.size(); i++) {
            CompilationErrorInfo error = errors.get(i);
            messageBuilder.append("- line ")
                    .append(error.getLineNumber())
                    .append(": ")
                    .append(error.getMessage())
                    .append("\n");
            if (i == 0) {
                messageBuilder.append(CodeSnippetUtils.generateCodeSnippet(
                                metaFileContent.lines().collect(Collectors.toList()),
                                (int) error.getLineNumber()))
                        .append("\n");
            }
        }
        return messageBuilder.toString();
    }

    private String generateMetaFileContent(String originalLibraryContent) {
        String metaFileContent = evaluate(originalLibraryContent);

        return metaFileContent;
    }

    private void createMetaTestFile(File metaWorkingDir, String metaFileContent) throws Exception {
        File metaFile = new File(metaWorkingDir + "/Library.java");
        if (!metaFile.createNewFile()) {
            throw new IOException("Failed to create a meta file.");
        }
        FilesUtils.writeToFile(metaFile, metaFileContent);
    }

    private Result runMetaTest(Context ctx, DirectoryConfiguration dirCfg, File metaWorkingDir) {
        DirectoryConfiguration metaDirCfg = new DirectoryConfiguration(
                metaWorkingDir.toString(),
                dirCfg.getOutputDir()
        );

        ContextDirector director = new ContextDirector(new ContextBuilder());
        Context metaCtx = director.constructWithLibrariesAndClassLoader(
                ctx.getCleanClassloader(),
                Action.META_TEST,
                metaDirCfg,
                ctx.getLibrariesToBeIncludedInCompilation()
        );

        ExecutionFlow flow = ExecutionFlow.build(metaCtx);
        Result metaResult = flow.run();

        return metaResult;
    }
}
