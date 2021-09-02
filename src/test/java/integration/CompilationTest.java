package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static testutils.ResultTestAssertions.*;

public class CompilationTest extends IntegrationTestBase {

    @Test
    void compilationOk() {
        Result result = run2(Action.TESTS, "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result).has(successfulCompilation());
    }

    @Test
    void compilationFailsDuringGradingMeans0() {
        Result result = run2(Action.FULL_WITH_HINTS, "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError", "ArrayUtilsInGradingMode");

        assertThat(result)
                .has(failedCompilation())
                .has(finalGrade(0))
                .has(compilationErrorInLine(29))
                .has(compilationErrorType("not a statement"))
                .has(compilationErrorType("';' expected"))
                .doesNotHave(compilationErrorMoreTimes("cannot find symbol", 2))
                .doesNotHave(testsExecuted());
    }

    @Test
    void compilationWithManyFailures() {
        Result result = run2(Action.TESTS,"MathArraysLibrary","MathArraysDifferentCompilationErrors");
        assertThat(result)
                .has(failedCompilation())
                .has(finalGrade(0))
                .has(compilationErrorInLine(21))
                .has(compilationErrorInLine(25))
                .has(compilationErrorInLine(33))
                .has(compilationErrorMoreTimes("cannot find symbol", 3))
                .doesNotHave(testsExecuted());
    }

    @Test
    void compilationErrorIsInTheConfigFile() {
        Result result = run2(Action.TESTS,"ListUtilsLibrary", "ListUtilsCompilationSuccess", "ListUtilsConfigWithCompilationError");
        assertThat(result)
                .has(failedCompilation())
                .has(configurationError())
                .doesNotHave(testsExecuted());
    }


}
