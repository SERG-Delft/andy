package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.writer.ResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.CodeSnippetGenerator;
import nl.tudelft.cse1110.andy.writer.standard.RandomAsciiArtGenerator;
import nl.tudelft.cse1110.andy.writer.standard.StandardResultWriter;
import nl.tudelft.cse1110.andy.writer.standard.VersionInformation;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.readFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static unit.writer.standard.StandardResultTestAssertions.compilationErrorOnLine;

public class CompilationTest extends IntegrationTestBase {
    protected VersionInformation versionInformation = new VersionInformation("testVersion", "testBuildTimestamp", "testCommitId");
    protected RandomAsciiArtGenerator asciiArtGenerator = mock(RandomAsciiArtGenerator.class);
    protected CodeSnippetGenerator codeSnippetGenerator = new CodeSnippetGenerator();

    protected ResultWriter writer;

    protected ResultWriter buildWriter() {
        return new StandardResultWriter(versionInformation, asciiArtGenerator, codeSnippetGenerator);
    }

    @BeforeEach
    void createWriter() {
        this.writer = buildWriter();
    }

    protected String generatedResult() {
        return readFile(new File(concatenateDirectories(reportDir.toString(), "stdout.txt")));
    }

    protected void writeResult(Result result) {
        writer.write(ctx, result);
    }

    @Test
    void compilationOk() {
        Result result = run(Action.TESTS, "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result.getCompilation().successful()).isTrue();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();
    }

    @Test
    void compilationFailsDuringGradingMeans0() {
        Result result = run(Action.FULL_WITH_HINTS, "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError", "ArrayUtilsInGradingMode");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(0);
        assertThat(result.getTests().wasExecuted()).isFalse();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();

        assertThat(result)
                .has(compilationErrorInLine(29))
                .has(compilationError("not a statement"))
                .has(compilationError("';' expected"));
    }

    @Test
    void compilationWithManyFailures() {
        Result result = run(Action.TESTS, "MathArraysLibrary", "MathArraysDifferentCompilationErrors");

        writeResult(result);

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getFinalGrade()).isEqualTo(0);
        assertThat(result.getTests().wasExecuted()).isFalse();
        assertThat(result.getGenericFailure().hasFailure()).isFalse();

        assertThat(result)
                .has(compilationErrorInLine(21))
                .has(compilationErrorInLine(25))
                .has(compilationErrorInLine(33))
                .has(sameCompilationErrorManyTimes("cannot find symbol", 3));

        // Verify that the code snippet is correct and is printed only for the first error
        String output = generatedResult();
        assertThat(output)
                .has(compilationErrorOnLine(21))
                .has(compilationErrorOnLine(25))
                .has(compilationErrorOnLine(33))
                .containsOnlyOnce("""
                            @MethodSource("generator")
                            void unique(String description, double[] array, double[] expectedArray) {
                        -->     assertThat(MathArrays.unique(array)).isEqual(expectedArray);
                            }
                        """)
                .containsOnlyOnce("-->")
                .doesNotContain("tc2 = Arguments.of(");
    }

    @Test
    void compilationErrorIsInTheConfigFile() {
        Result result = run(Action.TESTS, "ListUtilsLibrary", "ListUtilsCompilationSuccess", "ListUtilsConfigWithCompilationError");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getCompilation().hasConfigurationError()).isTrue();
        assertThat(result.getTests().wasExecuted()).isFalse();
    }


    private static Condition<Result> compilationErrorInLine(int line) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().anyMatch(e -> e.getLineNumber() == line);
            }
        };
    }

    private static Condition<Result> compilationError(String error) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().anyMatch(e -> e.getMessage().equals(error));
            }
        };
    }

    private static Condition<Result> sameCompilationErrorManyTimes(String errorType, int times) {
        return new Condition<>() {
            @Override
            public boolean matches(Result value) {
                return value.getCompilation().getErrors().stream().filter(e -> e.getMessage().contains(errorType)).count() == times;
            }
        };
    }


}
