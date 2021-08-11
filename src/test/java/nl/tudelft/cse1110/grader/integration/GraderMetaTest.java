package nl.tudelft.cse1110.grader.integration;

import org.junit.jupiter.api.Test;

import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestAssertions.*;
import static nl.tudelft.cse1110.grader.integration.GraderIntegrationTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class GraderMetaTest extends GraderIntegrationTestBase {

    @Test
    void testAllMetaTestsPassing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAdd");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(4));
    }

    @Test
    void testSomeMetaTestFailing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAdd");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(1))
                .has(metaTestFailing("AppliesMultipleCarriesWronglyMeta"))
                .has(metaTestFailing("DoesNotApplyCarryAtAllMeta"))
                .has(metaTestFailing("DoesNotApplyLastCarryMeta"));
    }

    @Test
    void testAllMetaTestsFailing() {
        String result = run(withMeta(), "NumberUtilsAddLibrary", "NumberUtilsNoTests", "NumberUtilsAdd");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(0))
                .has(metaTestFailing("AppliesMultipleCarriesWronglyMeta"))
                .has(metaTestFailing("DoesNotApplyCarryAtAllMeta"))
                .has(metaTestFailing("DoesNotApplyLastCarryMeta"))
                .has(metaTestFailing("DoesNotCheckNumbersOutOfRangeMeta"));
    }

    @Test
    void testMetaWhenMultipleClassesInLibrary() {
        String result = run(withMeta(), "SoftwhereLibrary", "SoftwhereMissingTests", "Softwhere");

        assertThat(result)
                .has(metaTests(4))
                .has(metaTestsPassing(3))
                .has(metaTestFailing("DoesNotCheckInvalidTripIdMeta"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        String result = run(withMeta(), "ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOf");

        assertThat(result)
                .has(metaTests(3))
                .has(metaTestsPassing(3));
    }

}