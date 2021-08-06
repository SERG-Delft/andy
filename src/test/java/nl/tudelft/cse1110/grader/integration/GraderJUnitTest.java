package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// This integration test class handles normal @Tests, which all compile. (the final test combines normal @Tests, parameterized tests and pbt though)
// See GraderCompilationTest for integration tests for compilation errors.
public class GraderJUnitTest extends GraderIntegrationTestBase {


    @Test
    void allTestsPassing() {            // 4/4 normal @Tests passing

        String result = run(justTests(), noScript(), "NumberUtilsAddLibrary", "NUmberUtilsAddAllTestsPass");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(4))
                .has(totalNumberOfJUnitTests(4));

    }


    // TODO: this one might even be another feature, as we probably want to give a better message to the student, e.g., "We do not see tests, are you sure you wrote them?" or something like that.
    // example: student forgets @Test
    @Test
    void noTests() {        // 0/0 normal @Tests passing

        String result = run(justTests(), noScript(), "NumberUtilsAddLibrary", "NumberUtilsNoTests");

        assertThat(result)
                .has(noJUnitTests());
    }


    // In test 2, assertFalse should be assertTrue.
    @Test
    void singleTestFailing() {

        String result = run(justTests(), noScript(), "LeapYearLibrary", "LeapYearSingleTestFails");  // 3/4 normal @Tests passing

        assertThat(result)
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(4))
                .has(failingTestName("leapCenturialYears"))
                .has(errorType("AssertionFailedError"));
    }


    // In test 1, the expected int should be 2, instead of 1.
    // In test 2, the expected int should be 1, instead of 2.
    @Test
    void allTestsFailing() {

        String result = run(justTests(), noScript(), "CountLettersLibrary", "CountLettersAllTestsFail");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(2))
                .has(failingTestName("multipleMatchingWords"))
                .has(failingTestName("lastWordDoesNotMatch"))
                .has(errorType("AssertionFailedError"));
    }


    // In test 1 and 2,  30*3 should be 30+50
    // In test 3, 30+50 should be 30*3
    // example: student misinterpreted the source code.
    @Test
    void someTestsFailing() {           // 1/4 normal @Tests passing

        String result = run(justTests(), noScript(), "PlayerPointsLibrary", "PlayerPointsSomeTestsFail");

        assertThat(result)
                .has(numberOfJUnitTestsPassing(1))
                .has(totalNumberOfJUnitTests(4))
                .has(failingTestName("lessPoints"))
                .has(failingTestName("manyPointsAndManyLives"))
                .has(failingTestName("manyPointsButLittleLives"))
                .has(errorType("AssertionFailedError"));

    }


    // test class contains normal @Tests, parameterized tests and pbt.
    @Test
    void ThreeDifferentTestTypesUsed() {

        String result = run(justTests(), noScript(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfDifferentTestTypes");  // 5/5 @Tests passing

        assertThat(result)
                .has(numberOfJUnitTestsPassing(5))
                .has(totalNumberOfJUnitTests(5));

    }


}
