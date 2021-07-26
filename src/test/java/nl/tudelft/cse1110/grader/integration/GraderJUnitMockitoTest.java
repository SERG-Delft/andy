package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GraderJUnitMockitoTest extends GraderIntegrationTestBase {



    // error in @Test1: instead of completeTodo(), addTodo() should be invoked.
    @Test
    void methodVerifiedButNotInvoked() {

        String result = run(justTests(), noScript(), "junit/mockitoMethodNotInvoked");  // 2/3 normal @Tests passing

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

        String result = run(justTests(), noScript(), "junit/misusingMockitoStubs");  // 2/3 normal @Tests passing

        assertThat(result)
                .has(numberOfJUnitTestsPassing(2))
                .has(totalNumberOfJUnitTests(3))
                .has(failingTestName("getNextReturnsFirst"))
                .has(errorType("mockito.exceptions.misusing"));
    }




}
