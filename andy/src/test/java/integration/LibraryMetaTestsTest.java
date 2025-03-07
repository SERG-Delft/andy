package integration;

import nl.tudelft.cse1110.andy.execution.step.RunMetaTestsStep;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static unit.writer.standard.StandardResultTestAssertions.containsString;


public class LibraryMetaTestsTest extends BaseMetaTestsTest {

    @Test
    void allMetaTestsPassing() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(4);
        assertThat(result.getMetaTests())
                .has(passedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(passedMetaTest("DoesNotApplyCarryAtAll"))
                .has(passedMetaTest("DoesNotApplyLastCarry"))
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void someMetaTestFailing() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(1);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void someMetaTestFailingWithWeights() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithWeight");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(7);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(2);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void testMetaWhenMultipleClassesInLibrary() {
        Result result = run("SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("BoundaryCheck"))
                .has(passedMetaTest("DoesNotCheckCapacity"))
                .has(passedMetaTest("DoesNotCheckSave"))
                .has(failedMetaTest("DoesNotCheckInvalidTripId"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        Result result = run("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(3);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("AlwaysReturnsNotFound"))
                .has(passedMetaTest("AlwaysReturnsStartIndex"))
                .has(passedMetaTest("DoesNotUseStartIndex"));
    }

    @Test
    void metaTestInternalFailure() {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithMetaTestInternalFailure");

        assertThat(result.hasGenericFailure()).isTrue();
        assertThat(result.getGenericFailure().getStepName())
                .isPresent()
                .get()
                .isEqualTo(RunMetaTestsStep.class.getSimpleName());
        assertThat(result.getGenericFailure().getExceptionMessage())
                .hasValueSatisfying(containsString("Meta test failed to find this text replacement"));
    }

    @ParameterizedTest
    @CsvSource({"NumberUtilsAddConfigurationWithMetaTestCompilationError", "NumberUtilsAddConfigurationWithPenaltyMetaTestCompilationError"})
    void compilationErrorInMetaTest(String config) {
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", config);

        assertThat(result.hasGenericFailure()).isTrue();
        assertThat(result.getGenericFailure().getExceptionMessage())
                .hasValueSatisfying(containsString("Meta test 'BadMetaTest' failed to compile"))
                .hasValueSatisfying(containsString("- line 35: ';' expected"))
                .hasValueSatisfying(containsString("--> Collections.reverse(reversedLeft)\n"));
    }

    @Test
    void allPenaltyMetaTestsPassing() {
        Result result = run("NumberUtilsAddLibrary",
                "NumberUtilsAddOfficialSolution", "NumberUtilsAddWithPenaltyMetaTestsConfiguration");

        assertThat(result.getPenaltyMetaTests().getTotalTests()).isEqualTo(3);
        assertThat(result.getPenaltyMetaTests().getPassedMetaTests()).isEqualTo(3);
        assertThat(result.getPenaltyMetaTests())
                .has(passedMetaTest("DoesNotApplyCarryAtAll"))
                .has(passedMetaTest("DoesNotApplyLastCarry"))
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void somePenaltyMetaTestFailing() {
        Result result = run("NumberUtilsAddLibrary",
                "NumberUtilsAddAllTestsPass", "NumberUtilsAddWithPenaltyMetaTestsConfiguration");

        assertThat(result.getPenaltyMetaTests().getTotalTests()).isEqualTo(3);
        assertThat(result.getPenaltyMetaTests().getPassedMetaTests()).isEqualTo(1);

        assertThat(result.getPenaltyMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void somePenaltyMetaTestFailingWithWeights() {
        Result result = run("NumberUtilsAddLibrary",
                "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithWeightWithPenaltyMetaTests");

        assertThat(result.getPenaltyMetaTests().getTotalTests()).isEqualTo(7);
        assertThat(result.getPenaltyMetaTests().getPassedMetaTests()).isEqualTo(2);

        assertThat(result.getPenaltyMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void metaTestsWithMockitoAndCustomException() {
        Result result = run("MockingAssignmentWithCustomExceptionLibrary", "MockingAssignmentWithCustomExceptionWrongWithoutAssertions", "MockingAssignmentWithCustomExceptionConfiguration");

        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(0);
        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(2);
    }

}