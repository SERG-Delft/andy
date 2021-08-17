package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.ExecutionStepHelper;
import nl.tudelft.cse1110.IntegrationTestBase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.ExecutionStepHelper.onlyJUnitTests;
import static nl.tudelft.cse1110.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunJUnitTestsStepTest {

    @Nested
    class TraditionalJUnitTests extends IntegrationTestBase {

        // 4/4 normal @Tests passing
        @Test
        void allTestsPassing() {

            String result = run(onlyJUnitTests(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(4))
                    .has(totalNumberOfJUnitTests(4));

        }

        // In test 2, assertFalse should be assertTrue.
        @Test
        void singleTestFailing() {

            String result = run(onlyJUnitTests(), "LeapYearLibrary", "LeapYearSingleTestFails");  // 3/4 normal @Tests passing

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

            String result = run(onlyJUnitTests(), "CountLettersLibrary", "CountLettersAllTestsFail");

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
        // 1/4 normal @Tests passing
        @Test
        void someTestsFailing() {

            String result = run(onlyJUnitTests(), "PlayerPointsLibrary", "PlayerPointsSomeTestsFail");

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
        void threeDifferentTestTypesUsed() {

            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfDifferentTestTypes");  // 5/5 @Tests passing

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(5))
                    .has(totalNumberOfJUnitTests(5));

        }


        // example: student forgets @Test
        // 0/0 normal @Tests passing
        @Test
        void noTests() {
            String result = run(onlyJUnitTests(), "NumberUtilsAddLibrary", "NumberUtilsNoTests");
            assertThat(result)
                    .has(errorMessage("We do not see any tests. Are you sure you wrote them?"));
        }
    }


    @Nested
    class Mockito extends IntegrationTestBase {
        // error in @Test1: instead of completeTodo(), addTodo() should be invoked.
        @Test
        void methodVerifiedButNotInvoked() {

            String result = run(ExecutionStepHelper.onlyJUnitTests(), "TodoApplicationLibrary", "TodoApplicationMockitoMethodNotInvoked");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(2))
                    .has(totalNumberOfJUnitTests(3))
                    .has(failingTestName("addTodoTest"))
                    .has(uninvokedMethod("todoService.completeTodo"))
                    .has(hintAtInteractionFound("todoService.addTodo"));
        }

        // error in @Test 3: student is misusing Mockito stubs in line 48: TheQueue q is not a mock, thus its methods cannot be stubbed!
        @Test
        void stubbingNonMockClass() {

            String result = run(ExecutionStepHelper.onlyJUnitTests(), "TheQueueLibrary", "TheQueueMisusingMockitoStub");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(2))
                    .has(totalNumberOfJUnitTests(3))
                    .has(failingTestName("getNextReturnsFirst"))
                    .has(errorType("mockito.exceptions.misusing"));
        }
    }


    @Nested
    class ParameterizedTests extends IntegrationTestBase {


        // in test case 3, the expected boolean should be flipped.
        @Test
        void singleParameterizedTestFails() {

            String result = run(onlyJUnitTests(), "PassingGradeLibrary", "PassingGradeSingleParameterizedTestFails");  // 4/5 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(4))
                    .has(totalNumberOfJUnitTests(5))
                    .has(failingParameterizedTestName("passed"))
                    .has(parameterizedTestCaseNumber(3))
                    .has(errorType("AssertionFailedError"));
        }


        // in test cases 1 and 2 of "validTest", 22 and 44 should be numbers in (20, 200) divisible by 20.
        // in test case 1 of "invalidTest", 40 should not be divisible by 20.
        @Test
        void moreParameterizedTestsFail() {

            String result = run(onlyJUnitTests(), "ATMLibrary", "ATMMoreParameterizedTestsFail");  // 11/14 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(11))
                    .has(totalNumberOfJUnitTests(14))
                    .has(failingParameterizedTestName("validTest"))
                    .has(failingParameterizedTestName("invalidTest"))
                    .has(parameterizedTestCaseNumber(1))
                    .has(parameterizedTestCaseNumber(2))
                    .has(errorType("AssertionFailedError"));
        }



        // Student accidentally passed the first argument (int result) as 3rd argument, making all tests fail.
        @Test
        void allParameterizedTestsFail() {

            String result = run(onlyJUnitTests(), "TwoIntegersLibrary", "TwoIntegersAllParameterizedTestsFail");  // 0/6 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(0))
                    .has(totalNumberOfJUnitTests(6))
                    .has(failingParameterizedTestName("sumValidCases"))
                    .has(parameterizedTestCaseNumber(1))
                    .has(parameterizedTestCaseNumber(2))
                    .has(parameterizedTestCaseNumber(3))
                    .has(parameterizedTestCaseNumber(4))
                    .has(parameterizedTestCaseNumber(5))
                    .has(parameterizedTestCaseNumber(6))
                    .has(errorType("AssertionFailedError"))
                    .has(errorType("IllegalArgumentException"));  // method will throw exception (See Library.java)
        }


        @Test
        void helperMethodInTestShouldPass() {

            String result = run(onlyJUnitTests(), "PiecewiseLibrary", "PiecewiseHelperInTest");  // 26/26 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(26))
                    .has(totalNumberOfJUnitTests(26));
        }


        // student reversed the 2 method names "invalidInputs" and "validInputs",
        //  raising exceptions since the number of arguments passed is incorrect, and thus making all tests fail.
        // in "invalidInputs", an exception is expected, whereas this is not being thrown by the method.
        // in "validInputs", exceptions are raised, whereas this is not expected.
        @Test
        void exceptionThrownByTest() {

            String result = run(onlyJUnitTests(), "MScAdmisisionLibrary", "MScAdmissionParameterizedTestThrowsException");  // 0/5 parameterized test cases

            assertThat(result)
                    .has(numberOfJUnitTestsPassing(0))
                    .has(totalNumberOfJUnitTests(5))
                    .has(failingParameterizedTestName("validInputs"))
                    .has(failingParameterizedTestName("invalidInputs"))
                    .has(parameterizedTestCaseNumber(1))
                    .has(parameterizedTestCaseNumber(2))
                    .has(parameterizedTestCaseNumber(3))
                    .has(errorType("ParameterResolutionException"))
                    .has(errorType("AssertionError"))
                    .has(errorMessage("Expecting code to raise a throwable."));
        }


    }

    @Nested
    class JQWik extends IntegrationTestBase {


        @Test
        void testSimplePropertyTest() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfSimpleJqwikError");
            assertThat(result)
                    .has(propertyTestFailing("testNoElementInWholeArray"));
        }


        @Test
        void testMultiplePropertyTestsFailing() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfMultipleJqwikErrors");
            assertThat(result)
                    .has(propertyTestFailing("testNoElementInWholeArray"))
                    .has(propertyTestFailing("testValueInArrayUniqueElements"));
        }


        @Test
        void testMultiplePropertyWithParameterizedTests() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJqwikWithParameterized");
            assertThat(result)
                    .has(propertyTestFailing("testNoElementInWholeArray"))
                    .has(propertyTestFailing("testValueInArrayUniqueElements"))
                    .has(parameterizedTestFailing("test", 6));
        }


        @Test
        void testMessageOtherThanAssertionError() {
            String result = run(onlyJUnitTests(), "NumberUtilsAddPositiveLibrary", "NumberUtilsAddPositiveJqwikException");
            assertThat(result)
                    .has(propertyTestFailing("testAddition"));
        }
    }

}
