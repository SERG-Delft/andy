package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.numberOfJUnitTestsPassing;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.totalNumberOfJUnitTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderSmokeTest extends GraderIntegrationTestBase {

    @Test
    void smoke() {
        String result = run(justTests(), noScript(), "smoke");
        assertThat(result)
                .has(numberOfJUnitTestsPassing(31))
                .has(totalNumberOfJUnitTests(31));
    }
}
