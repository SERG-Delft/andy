package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderCompilationTest extends GraderIntegrationTestBase {

    @Test
    void compilationFailure() {
        String result = run(justCompilation(), noScript(), "failure");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.compilationFailure())
                .has(GraderIntegrationTestAssertions.compilationErrorOnLine(39))
                .has(GraderIntegrationTestAssertions.compilationErrorType("illegal start of type"));
    }

}
