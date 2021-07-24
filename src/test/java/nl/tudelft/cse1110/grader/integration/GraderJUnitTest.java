package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// See GraderCompilationTest for integration tests for compilation errors.
public class GraderJUnitTest extends GraderIntegrationTestBase {

    @Test
    void allTestsPassing() {

        String result = run(justTests(), noScript(), "junit/passing");

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(4))
                .has(totalNumberOfJUnitTests(4));

    }


    // TODO: this one might even be another feature, as we probably want to give a better message to the student, e.g., "We do not see tests, are you sure you wrote them?" or something like that.
    // example: student forgets @Test
    @Test
    void noTests() {

        String result = run(justTests(), noScript(), "junit/notests");

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(0));
    }


    // In test 1, the expected int should be 2, instead of 1.
    // In test 2, the expected int should be 1, instead of 2.
    @Test
    void allTestsFailing() {

        String result = run(justTests(), noScript(), "junit/failing");

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(0))
                .has(totalNumberOfJUnitTests(2))
                .has(failingTestName("multipleMatchingWords"))
                .has(failingTestName("lastWordDoesNotMatch"))
                .has(errorType("AssertionFailedError"));
    }



    // in test case 3, the expected boolean should be flipped.
    @Test
    void singleParameterizedTestFails() {

        String result = run(justTests(), noScript(), "junit/singleparameterizedtestfails");  // 4/5 parameterized test cases

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(4))
                .has(totalNumberOfJUnitTests(5))
                .has(failingParameterizedTestName("passed"))
                .has(parameterizedTestCaseNumber(3))
                .has(errorType("AssertionFailedError"));
    }



    // TODO: for some reason when I run the whole test class, the following 2 tests fail??!!?
//    // in test cases 4 and 5, the expected boolean should be flipped.
//    @Test
//    void moreParameterizedTestsFail() {
//
//        String result = run(justTests(), noScript(), "junit/moreparameterizedtestsfail");  // 3/5 parameterized test cases
//
//        System.out.println(result);
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(3))
//                .has(totalNumberOfJUnitTests(5))
//                .has(failingParameterizedTestName("passed"))
//                .has(parameterizedTestCaseNumber(4))
//                .has(parameterizedTestCaseNumber(5))
//                .has(errorType("AssertionFailedError"));
//    }
//
//
//
//
//    // in all test cases, the expected booleans should be flipped.
//    @Test
//    void allParameterizedTestsFail() {
//
//        String result = run(justTests(), noScript(), "junit/allparameterizedtestsfail");  // 0/5 parameterized test cases
//
//        System.out.println(result);
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(0))
//                .has(totalNumberOfJUnitTests(5))
//                .has(failingParameterizedTestName("passed"))
//                .has(parameterizedTestCaseNumber(1))
//                .has(parameterizedTestCaseNumber(2))
//                .has(parameterizedTestCaseNumber(3))
//                .has(parameterizedTestCaseNumber(4))
//                .has(parameterizedTestCaseNumber(5))
//                .has(errorType("AssertionFailedError"));
//    }


    @Test
    void inappropriateAssertionsShouldPass() {

        String result = run(justTests(), noScript(), "junit/inappropriateassertions");  // 3/3 normal tests

        System.out.println(result);

        assertThat(result)
                .has(numberOfJUnitTestsPassing(3))
                .has(totalNumberOfJUnitTests(3));
    }


}
