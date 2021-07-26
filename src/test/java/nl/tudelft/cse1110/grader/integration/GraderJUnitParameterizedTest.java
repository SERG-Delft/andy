package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;



public class GraderJUnitParameterizedTest extends GraderIntegrationTestBase {



//    // in test case 3, the expected boolean should be flipped.
//    @Test
//    void singleParameterizedTestFails() {
//
//        String result = run(justTests(), noScript(), "junit/singleParameterizedTestFails");  // 4/5 parameterized test cases
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(4))
//                .has(totalNumberOfJUnitTests(5))
//                .has(failingParameterizedTestName("passed"))
//                .has(parameterizedTestCaseNumber(3))
//                .has(errorType("AssertionFailedError"));
//    }
//
//
//
//    // in test cases 1 and 2 of "validTest", 22 and 44 should be numbers in (20, 200) divisible by 20.
//    // in test case 1 of "invalidTest", 40 should not be divisible by 20.
//    @Test
//    void moreParameterizedTestsFail() {
//
//        String result = run(justTests(), noScript(), "junit/moreParameterizedTestsFail");  // 11/14 parameterized test cases
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(11))
//                .has(totalNumberOfJUnitTests(14))
//                .has(failingParameterizedTestName("validTest"))
//                .has(failingParameterizedTestName("invalidTest"))
//                .has(parameterizedTestCaseNumber(1))
//                .has(parameterizedTestCaseNumber(2))
//                .has(errorType("AssertionFailedError"));
//    }
//
//
//
//
//    // Student accidentally passed the first argument (int result) as 3rd argument, making all tests fail.
//    @Test
//    void allParameterizedTestsFail() {
//
//        String result = run(justTests(), noScript(), "junit/allParameterizedTestsFail");  // 0/6 parameterized test cases
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(0))
//                .has(totalNumberOfJUnitTests(6))
//                .has(failingParameterizedTestName("sumValidCases"))
//                .has(parameterizedTestCaseNumber(1))
//                .has(parameterizedTestCaseNumber(2))
//                .has(parameterizedTestCaseNumber(3))
//                .has(parameterizedTestCaseNumber(4))
//                .has(parameterizedTestCaseNumber(5))
//                .has(parameterizedTestCaseNumber(6))
//                .has(errorType("AssertionFailedError"))
//                .has(errorType("IllegalArgumentException"));  // method will throw exception (See Library.java)
//    }
//
//
//    @Test
//    void helperMethodInTestShouldPass() {
//
//        String result = run(justTests(), noScript(), "junit/helperMethodInTest");  // 26/26 parameterized test cases
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(26))
//                .has(totalNumberOfJUnitTests(26));
//    }
//
//
//
//    // student reversed the 2 method names "invalidInputs" and "validInputs",
//    //  raising exceptions since the number of arguments passed is incorrect, and thus making all tests fail.
//    // in "invalidInputs", an exception is expected, whereas this is not being thrown by the method.
//    // in "validInputs", exceptions are raised, whereas this is not expected.
//    @Test
//    void exceptionThrownByTest() {
//
//        String result = run(justTests(), noScript(), "junit/exceptionThrownByTest");  // 0/5 parameterized test cases
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(0))
//                .has(totalNumberOfJUnitTests(5))
//                .has(failingParameterizedTestName("validInputs"))
//                .has(failingParameterizedTestName("invalidInputs"))
//                .has(parameterizedTestCaseNumber(1))
//                .has(parameterizedTestCaseNumber(2))
//                .has(parameterizedTestCaseNumber(3))
//                .has(errorType("ParameterResolutionException"))
//                .has(errorType("AssertionError"))
//                .has(errorMessage("Expecting code to raise a throwable."));
//    }
//
//
//    @Test
//    void exceptionNotThrownByMethod() {
//
//        String result = run(justTests(), noScript(), "junit/exceptionThrownByTest");  // 0/5 parameterized test cases
//
//        System.out.println(result);
//
//        assertThat(result)
//                .has(numberOfJUnitTestsPassing(0))
//                .has(totalNumberOfJUnitTests(5))
//                .has(failingParameterizedTestName("validInputs"))
//                .has(failingParameterizedTestName("invalidInputs"))
//                .has(parameterizedTestCaseNumber(1))
//                .has(parameterizedTestCaseNumber(2))
//                .has(parameterizedTestCaseNumber(3))
//                .has(errorType("ParameterResolutionException"))
//                .has(errorType("AssertionError"))
//                .has(errorMessage("Expecting code to raise a throwable."));
//
//    }



}
