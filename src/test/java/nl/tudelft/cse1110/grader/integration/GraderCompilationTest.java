package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.numberOfJUnitTestsPassing;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.totalNumberOfJUnitTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderCompilationTest extends GraderIntegrationTestBase {


    @Test
    void compilationFailure() {
        String result = run(justCompilation(), "ArrayUtilsIsSortedLibrary", "ArrayUtilsIsSortedWithCompilationError");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationFailure())
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(29))
                .has(GraderIntegrationTestAssertions.compilationErrorType("not a statement"))
                .has(GraderIntegrationTestAssertions.compilationErrorType("';' expected"));
    }


    @Test
    void compilationSuccess() {
        String result = run(justCompilation(),  "ListUtilsLibrary", "ListUtilsCompilationSuccess");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationSuccess());
    }


    @Test
    void compilationDifferentFailures() {
        String result = run(justCompilation(), "MathArraysLibrary","MathArraysDifferentCompilationErrors");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationFailure())
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(21))
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(25))
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(33))
                .has(GraderIntegrationTestAssertions.compilationErrorMoreTimes("cannot find symbol", 3));
    }

}
