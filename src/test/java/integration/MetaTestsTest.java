package integration;

import org.junit.jupiter.api.Test;

import static utils.ResultTestAssertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MetaTestsTest extends IntegrationTestBase {

    @Test
    void testAllMetaTestsPassing() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

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
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(1))
                .has(metaTestPassing("DoesNotCheckNumbersOutOfRange"))
                .has(metaTestFailing("AppliesMultipleCarriesWrongly"))
                .has(metaTestFailing("DoesNotApplyCarryAtAll"))
                .has(metaTestFailing("DoesNotApplyLastCarry"));
    }

    @Test
    void testSomeMetaTestFailingWithWeights() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithWeight");

        assertThat(result)
                .has(metaTests(7))
                .has(metaTestsPassing(2))
                .has(metaTestPassing("DoesNotCheckNumbersOutOfRange"))
                .has(metaTestFailing("AppliesMultipleCarriesWrongly"))
                .has(metaTestFailing("DoesNotApplyCarryAtAll"))
                .has(metaTestFailing("DoesNotApplyLastCarry"));
    }

    @Test
    void testAllMetaTestsFailing() {
        String result = run("NumberUtilsAddLibrary", "NumberUtilsEmptyTests", "NumberUtilsAddConfiguration");

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
        String result = run("SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfiguration");

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
        String result = run("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfiguration");

        assertThat(result)
                .has(metaTests(3))
                .has(metaTestsPassing(3))
                .has(metaTestPassing("AlwaysReturnsNotFound"))
                .has(metaTestPassing("AlwaysReturnsStartIndex"))
                .has(metaTestPassing("DoesNotUseStartIndex"));
    }

}