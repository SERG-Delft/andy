package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.ExecutionStepHelper;
import nl.tudelft.cse1110.andy.IntegrationTestBase;
import nl.tudelft.cse1110.andy.ResultTestAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ExecutionStepHelper.onlyJUnitTests;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RunJUnitTestsStepTest {

    @Nested
    class TraditionalJUnitTests extends IntegrationTestBase {

        // 4/4 normal @Tests passing
        @Test
        void allTestsPassing() {

            String result = run(onlyJUnitTests(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(4))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(4));

        }

        // In test 2, assertFalse should be assertTrue.
        @Test
        void singleTestFailing() {

            String result = run(onlyJUnitTests(), "LeapYearLibrary", "LeapYearSingleTestFails");  // 3/4 normal @Tests passing

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(3))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(4))
                    .has(ResultTestAssertions.failingTestName("leapCenturialYears"))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"));
        }


        // In test 1, the expected int should be 2, instead of 1.
        // In test 2, the expected int should be 1, instead of 2.
        @Test
        void allTestsFailing() {

            String result = run(onlyJUnitTests(), "CountLettersLibrary", "CountLettersAllTestsFail");

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(0))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(2))
                    .has(ResultTestAssertions.failingTestName("multipleMatchingWords"))
                    .has(ResultTestAssertions.failingTestName("lastWordDoesNotMatch"))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"));
        }


        // In test 1 and 2,  30*3 should be 30+50
        // In test 3, 30+50 should be 30*3
        // example: student misinterpreted the source code.
        // 1/4 normal @Tests passing
        @Test
        void someTestsFailing() {

            String result = run(onlyJUnitTests(), "PlayerPointsLibrary", "PlayerPointsSomeTestsFail");

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(1))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(4))
                    .has(ResultTestAssertions.failingTestName("lessPoints"))
                    .has(ResultTestAssertions.failingTestName("manyPointsAndManyLives"))
                    .has(ResultTestAssertions.failingTestName("manyPointsButLittleLives"))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"));

        }


        // test class contains normal @Tests, parameterized tests and pbt.
        @Test
        void threeDifferentTestTypesUsed() {

            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfDifferentTestTypes");  // 5/5 @Tests passing

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(5))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(5));

        }


        // example: student forgets @Test
        // 0/0 normal @Tests passing
        @Test
        void noTests() {
            String result = run(onlyJUnitTests(), "NumberUtilsAddLibrary", "NumberUtilsNoTests");
            assertThat(result)
                    .has(ResultTestAssertions.errorMessage("We do not see any tests. Are you sure you wrote them?"));
        }

        //Check to see if the System.out.printlns are caught from the console
        @Test
        void consoleOutputCaught(){

            String result = run(onlyJUnitTests(), "ZagZigLibrary", "ZagZigRandomSysouts");

            assertThat(result)
                    .has(ResultTestAssertions.consoleOutputExists())
                    .has(ResultTestAssertions.consoleOutput("RandomString1"))
                    .has(ResultTestAssertions.consoleOutput("HelloWorld2"))
                    .has(ResultTestAssertions.consoleOutput("I love SQT3"));
        }

        //Check to see that there're no sysouts
        @Test
        void noConsoleOutputToBeCaught(){

            String result = run(onlyJUnitTests(), "ZagZigLibrary", "ZagZigNoSysouts");

            assertThat(result)
                    .doesNotHave(ResultTestAssertions.consoleOutputExists());
        }
    }

    @Nested
    class Mockito extends IntegrationTestBase {
        // error in @Test1: instead of completeTodo(), addTodo() should be invoked.
        @Test
        void methodVerifiedButNotInvoked() {

            String result = run(ExecutionStepHelper.onlyJUnitTests(), "TodoApplicationLibrary", "TodoApplicationMockitoMethodNotInvoked");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(2))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(3))
                    .has(ResultTestAssertions.failingTestName("addTodoTest"))
                    .has(ResultTestAssertions.uninvokedMethod("todoService.completeTodo"))
                    .has(ResultTestAssertions.hintAtInteractionFound("todoService.addTodo"));
        }

        // error in @Test 3: student is misusing Mockito stubs in line 48: TheQueue q is not a mock, thus its methods cannot be stubbed!
        @Test
        void stubbingNonMockClass() {

            String result = run(ExecutionStepHelper.onlyJUnitTests(), "TheQueueLibrary", "TheQueueMisusingMockitoStub");  // 2/3 normal @Tests passing

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(2))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(3))
                    .has(ResultTestAssertions.failingTestName("getNextReturnsFirst"))
                    .has(ResultTestAssertions.errorType("mockito.exceptions.misusing"));
        }
    }


    @Nested
    class ParameterizedTests extends IntegrationTestBase {


        // in test case 3, the expected boolean should be flipped.
        @Test
        void singleParameterizedTestFails() {

            String result = run(onlyJUnitTests(), "PassingGradeLibrary", "PassingGradeSingleParameterizedTestFails");  // 4/5 parameterized test cases

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(4))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(5))
                    .has(ResultTestAssertions.failingParameterizedTestName("passed"))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(3))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"));
        }


        // in test cases 1 and 2 of "validTest", 22 and 44 should be numbers in (20, 200) divisible by 20.
        // in test case 1 of "invalidTest", 40 should not be divisible by 20.
        @Test
        void moreParameterizedTestsFail() {

            String result = run(onlyJUnitTests(), "ATMLibrary", "ATMMoreParameterizedTestsFail");  // 11/14 parameterized test cases

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(11))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(14))
                    .has(ResultTestAssertions.failingParameterizedTestName("validTest"))
                    .has(ResultTestAssertions.failingParameterizedTestName("invalidTest"))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(1))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(2))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"));
        }



        // Student accidentally passed the first argument (int result) as 3rd argument, making all tests fail.
        @Test
        void allParameterizedTestsFail() {

            String result = run(onlyJUnitTests(), "TwoIntegersLibrary", "TwoIntegersAllParameterizedTestsFail");  // 0/6 parameterized test cases

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(0))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(6))
                    .has(ResultTestAssertions.failingParameterizedTestName("sumValidCases"))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(1))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(2))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(3))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(4))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(5))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(6))
                    .has(ResultTestAssertions.errorType("AssertionFailedError"))
                    .has(ResultTestAssertions.errorType("IllegalArgumentException"));  // method will throw exception (See Library.java)
        }


        @Test
        void helperMethodInTestShouldPass() {

            String result = run(onlyJUnitTests(), "PiecewiseLibrary", "PiecewiseHelperInTest");  // 26/26 parameterized test cases

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(26))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(26));
        }


        // student reversed the 2 method names "invalidInputs" and "validInputs",
        //  raising exceptions since the number of arguments passed is incorrect, and thus making all tests fail.
        // in "invalidInputs", an exception is expected, whereas this is not being thrown by the method.
        // in "validInputs", exceptions are raised, whereas this is not expected.
        @Test
        void exceptionThrownByTest() {

            String result = run(onlyJUnitTests(), "MScAdmisisionLibrary", "MScAdmissionParameterizedTestThrowsException");  // 0/5 parameterized test cases

            assertThat(result)
                    .has(ResultTestAssertions.numberOfJUnitTestsPassing(0))
                    .has(ResultTestAssertions.totalNumberOfJUnitTests(5))
                    .has(ResultTestAssertions.failingParameterizedTestName("validInputs"))
                    .has(ResultTestAssertions.failingParameterizedTestName("invalidInputs"))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(1))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(2))
                    .has(ResultTestAssertions.parameterizedTestCaseNumber(3))
                    .has(ResultTestAssertions.errorType("ParameterResolutionException"))
                    .has(ResultTestAssertions.errorType("AssertionError"))
                    .has(ResultTestAssertions.errorMessage("Expecting code to raise a throwable."));
        }


    }

    @Nested
    class JQWik extends IntegrationTestBase {


        @Test
        void testSimplePropertyTest() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfSimpleJqwikError");
            assertThat(result)
                    .has(ResultTestAssertions.propertyTestFailing("testNoElementInWholeArray"));
        }


        @Test
        void testMultiplePropertyTestsFailing() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfMultipleJqwikErrors");
            assertThat(result)
                    .has(ResultTestAssertions.propertyTestFailing("testNoElementInWholeArray"))
                    .has(ResultTestAssertions.propertyTestFailing("testValueInArrayUniqueElements"));
        }


        @Test
        void testMultiplePropertyWithParameterizedTests() {
            String result = run(onlyJUnitTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJqwikWithParameterized");
            assertThat(result)
                    .has(ResultTestAssertions.propertyTestFailing("testNoElementInWholeArray"))
                    .has(ResultTestAssertions.propertyTestFailing("testValueInArrayUniqueElements"))
                    .has(ResultTestAssertions.parameterizedTestFailing("test", 6));
        }


        @Test
        void testMessageOtherThanAssertionError() {
            String result = run(onlyJUnitTests(), "NumberUtilsAddPositiveLibrary", "NumberUtilsAddPositiveJqwikException");
            assertThat(result)
                    .has(ResultTestAssertions.propertyTestFailing("testAddition"));
        }
    }

}
