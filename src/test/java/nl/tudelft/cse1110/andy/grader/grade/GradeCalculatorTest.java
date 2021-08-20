package nl.tudelft.cse1110.andy.grader.grade;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;

public class GradeCalculatorTest {

    @ParameterizedTest
    @MethodSource("sameWeights")
    void withSameWeights(int coveredBranches, int totalBranches,
               int detectedMutations, int totalMutations,
               int metaTestsPassed, int totalMetaTests,
               int checksPassed, int totalChecks, int expectedGrade) {

        GradeWeight weights = new GradeWeight(false, 0.25f, 0.25f, 0.25f, 0.25f);

        GradeValues grades = new GradeValues();
        grades.setBranchGrade(coveredBranches, totalBranches);
        grades.setMutationGrade(detectedMutations, totalMutations);
        grades.setMetaGrade(metaTestsPassed, totalMetaTests);
        grades.setCheckGrade(checksPassed, totalChecks);

        int finalGrade = new GradeCalculator(weights).calculateFinalGrade(grades);

        assertThat(finalGrade).isEqualTo(expectedGrade);
    }

    private static Stream<Arguments> sameWeights() {
        return Stream.of(
                of(25, 25, 55, 55, 100, 100, 5, 5, 100), // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*1 = 1.0 --> 100
                of(22, 25, 45, 55, 82, 100, 4, 5, 83), // 0.25*(22/25) + 0.25*(45/55) + 0.25*(82/100) +  0.25*(4/5) = 0.8295... --> 83
                of(22, 25, 55, 55, 100, 100, 5, 5, 97), // 0.25*(22/25) + 0.25*1 + 0.25*1 +  0.25*1 = 0.97 --> 97
                of(25, 25, 45, 55, 100, 100, 5, 5, 95), // 0.25*1 + 0.25*(45/55) + 0.25*1 +  0.25*1 = 0.9545... --> 95
                of(25, 25, 55, 55, 35, 100, 5, 5, 84), // 0.25*1 + 0.25*1 + 0.25*(35/100) +  0.25*1 = 0.8375 --> 84
                of(25, 25, 55, 55, 100, 100, 2, 5, 85), // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*(2/5) = 0.85 --> 85
                of(25, 25, 50, 55, 100, 100, 5, 5, 98), // 0.25*1 + 0.25*(50/55) + 0.25*1 +  0.25*1 = 0.9773... --> 98
                of(0, 0, 55, 55, 100, 100, 5, 5, 100), // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*1 = 0.1 --> 100
                of(12, 12, 0, 0, 50, 100, 5, 5, 88), // 0.25*1 + 0.25*1 + 0.25*(50/100) +  0.25*1 = 0.875 --> 88
                of(24, 30, 55, 55, 0, 0, 5, 5, 95), // 0.25*(24/30) + 0.25*1 + 0.25*1 +  0.25*1 = 0.95 --> 95
                of(24, 30, 55, 55, 100, 100, 0, 0, 95), // 0.25*(24/30) + 0.25*1 + 0.25*1 +  0.25*1 = 0.95 --> 95
                of(0, 25, 0, 55, 0, 100, 0, 5, 0) // no scores
        );
    }

    @Test
    void failureGives0() {
        GradeWeight weights = new GradeWeight(true, 0.25f, 0.25f, 0.25f, 0.25f);

        GradeValues grades = new GradeValues();
        grades.setBranchGrade(10, 10);
        grades.setMutationGrade(10, 10);
        grades.setMetaGrade(10, 10);
        grades.setCheckGrade(10, 10);

        GradeCalculator calculator = new GradeCalculator(weights);
        calculator.failed();

        int finalGrade = calculator.calculateFinalGrade(grades);
        assertThat(finalGrade).isEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("differentWeights")
    void withDifferentWeights(int coveredBranches, int totalBranches,
               int detectedMutations, int totalMutations,
               int metaTestsPassed, int totalMetaTests,
               int checksPassed, int totalChecks, int expectedGrade) {

        GradeWeight weights = new GradeWeight(true, 0.4f, 0.3f, 0.2f, 0.1f);

        GradeValues grades = new GradeValues();
        grades.setBranchGrade(coveredBranches, totalBranches);
        grades.setMutationGrade(detectedMutations, totalMutations);
        grades.setMetaGrade(metaTestsPassed, totalMetaTests);
        grades.setCheckGrade(checksPassed, totalChecks);

        int finalGrade = new GradeCalculator(weights).calculateFinalGrade(grades);

        assertThat(finalGrade).isEqualTo(expectedGrade);
    }

    private static Stream<Arguments> differentWeights() {
        return Stream.of(
                of(25, 25, 55, 55, 100, 100, 5, 5, 100), // 0.4*1 + 0.3*1 + 0.2*1 +  0.1*1 = 1.0 --> 100
                of(22, 25, 45, 55, 82, 100, 4, 5, 84), // 0.4*(22/25) + 0.3*(45/55) + 0.2*(82/100) +  0.1*(4/5) = 0.8415... --> 84
                of(13, 25, 55, 55, 100, 100, 5, 5, 81), // 0.4*(13/25) + 0.3*1 + 0.2*1 +  0.1*1 = 0.808 --> 81
                of(25, 25, 45, 55, 100, 100, 5, 5, 95), // 0.4*1 + 0.3*(45/55) + 0.2*1 +  0.1*1 = 0.9454... --> 95
                of(25, 25, 55, 55, 35, 100, 5, 5, 87), // 0.4*1 + 0.3*1 + 0.2*(35/100) +  0.1*1 = 0.87 --> 87
                of(25, 25, 55, 55, 100, 100, 2, 5, 94) // 0.4*1 + 0.3*1 + 0.2*1 +  0.1*(2/5) = 0.94 --> 94
        );
    }
}
