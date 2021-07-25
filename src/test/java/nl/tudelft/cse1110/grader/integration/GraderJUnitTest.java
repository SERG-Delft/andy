package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// This integration test class handles normal @Tests, which all compile.
// See GraderCompilationTest for integration tests for compilation errors.
public class GraderJUnitTest extends GraderIntegrationTestBase {

    @Test
    void allTestsPassing() {

        String result = run(justTests(), noScript(), "junit/passing");  // 4/4 normal tests

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(4))
                .has(totalNumberOfJUnitTests(4));

    }


    // TODO: this one might even be another feature, as we probably want to give a better message to the student, e.g., "We do not see tests, are you sure you wrote them?" or something like that.
    // example: student forgets @Test
    @Test
    void noTests() {

        String result = run(justTests(), noScript(), "junit/noTests");  // 0/0 normal tests

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(0));
    }


    // In test 1, the expected int should be 2, instead of 1.
    // In test 2, the expected int should be 1, instead of 2.
    @Test
    void allTestsFailing() {

        String result = run(justTests(), noScript(), "junit/failing");  // 0/2 normal tests

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(2))
                .has(failingTestName("multipleMatchingWords"))
                .has(failingTestName("lastWordDoesNotMatch"))
                .has(errorType("AssertionFailedError"));
    }



    @Test
    void inappropriateAssertionsShouldPass() {

        String result = run(justTests(), noScript(), "junit/inappropriateAssertions");  // 3/3 normal tests

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(3));
    }




}
