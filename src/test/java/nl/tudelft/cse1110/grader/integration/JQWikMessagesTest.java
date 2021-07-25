package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.parameterizedTestFailing;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.justTests;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.noScript;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.propertyTestFailing;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.setRemoveAssertJRelatedElementsFromStackTrace;

public class JQWikMessagesTest extends GraderIntegrationTestBase {

    @Test
    void testSimplePropertyTest() {
        String result = run(justTests(), noScript(), "jqwik/jqwikSimple");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"));
    }

    @Test
    void testMultiplePropertyTestsFailing() {
        String result = run(justTests(), noScript(), "jqwik/jqwikMultiple");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"))
                .has(propertyTestFailing("testValueInArrayUniqueElements"));
    }

    @Test
    void testMultiplePropertyWithParameterizedTests() {
        String result = run(justTests(), noScript(), "jqwik/jqwikWithParameterized");
        assertThat(result)
                .has(propertyTestFailing("testNoElementInWholeArray"))
                .has(propertyTestFailing("testValueInArrayUniqueElements"))
                .has(parameterizedTestFailing("test", 6));
    }

}
