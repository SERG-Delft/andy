package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.andy.ResultTestAssertions.*;
import static nl.tudelft.cse1110.andy.ExecutionStepHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class RunMetaTestsStepTest extends IntegrationTestBase {

    @Test
    void testAllMetaTestsPassing() {
        String result = run(onlyMetaTests(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(4))
                .has(metaTestPassing("AppliesMultipleCarriesWrongly"))
                .has(metaTestPassing("DoesNotApplyCarryAtAll"))
                .has(metaTestPassing("DoesNotApplyLastCarry"))
                .has(metaTestPassing("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void testSomeMetaTestFailing() {
        String result = run(onlyMetaTests(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(1))
                .has(metaTestPassing("DoesNotCheckNumbersOutOfRange"))
                .has(metaTestFailing("AppliesMultipleCarriesWrongly"))
                .has(metaTestFailing("DoesNotApplyCarryAtAll"))
                .has(metaTestFailing("DoesNotApplyLastCarry"));
    }

    @Test
    void testAllMetaTestsFailing() {
        String result = run(onlyMetaTests(), "NumberUtilsAddLibrary", "NumberUtilsNoTests", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(0))
                .has(metaTestFailing("AppliesMultipleCarriesWrongly"))
                .has(metaTestFailing("DoesNotApplyCarryAtAll"))
                .has(metaTestFailing("DoesNotApplyLastCarry"))
                .has(metaTestFailing("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void testMetaWhenMultipleClassesInLibrary() {
        String result = run(onlyMetaTests(), "SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(3))
                .has(metaTestPassing("BoundaryCheck"))
                .has(metaTestPassing("DoesNotCheckCapacity"))
                .has(metaTestPassing("DoesNotCheckSave"))
                .has(metaTestFailing("DoesNotCheckInvalidTripId"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        String result = run(onlyMetaTests(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfiguration");

        assertThat(result)
                .has(metaTests(3))
                .has(metaTestsPassing(3))
                .has(metaTestPassing("AlwaysReturnsNotFound"))
                .has(metaTestPassing("AlwaysReturnsStartIndex"))
                .has(metaTestPassing("DoesNotUseStartIndex"));
    }

}