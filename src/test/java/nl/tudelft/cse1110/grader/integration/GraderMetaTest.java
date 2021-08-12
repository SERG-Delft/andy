package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class GraderMetaTest extends GraderIntegrationTestBase {

    @Test
    void testAllMetaTestsPassing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(4));
    }

    @Test
    void testSomeMetaTestFailing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(1))
                .has(metaTestFailing("AppliesMultipleCarriesWrongly"))
                .has(metaTestFailing("DoesNotApplyCarryAtAll"))
                .has(metaTestFailing("DoesNotApplyLastCarry"));
    }

    @Test
    void testAllMetaTestsFailing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsNoTests", "NumberUtilsAddConfiguration");

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
        String result = run(withMeta(), "SoftwhereLibrary", "SoftwhereMissingTests", "SoftwhereConfig");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(3))
                .has(metaTestFailing("DoesNotCheckInvalidTripId"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        String result = run(withMeta(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfig");

        assertThat(result)
                .has(metaTests(3))
                .has(metaTestsPassing(3));
    }

}