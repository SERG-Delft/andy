package nl.tudelft.cse1110.grader.execution.step;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderSmokeTest extends GraderIntegrationTestBase {

    @Test
    void smoke() {
        String result = run(GraderIntegrationTestHelper.justTests(), "NumberUtilsAddLibrary", "NumberUtilsAddSmoke");
        assertThat(result)
                .has(GraderIntegrationTestAssertions.numberOfJUnitTestsPassing(31))
                .has(GraderIntegrationTestAssertions.totalNumberOfJUnitTests(31));
    }

}
