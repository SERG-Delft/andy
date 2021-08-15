package nl.tudelft.cse1110.grader.unit;

import nl.tudelft.cse1110.grader.result.GradeCalculator;
import nl.tudelft.cse1110.grader.result.GradeValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GradeCalculatorTest {


    private GradeValues gradeValues1;
    private GradeValues gradeValues2;
    private GradeCalculator gradeCalculator1;
    private GradeCalculator gradeCalculator2;


    @BeforeEach
    void setUp() {
        gradeValues1 = new GradeValues(false,
                0.25f, 0.25f, 0.25f, 0.25f);
        gradeValues2 = new GradeValues(true,
                0.4f, 0.3f, 0.2f, 0.1f);
        gradeCalculator1 = new GradeCalculator(gradeValues1);
        gradeCalculator2 = new GradeCalculator(gradeValues2);
    }

    // helper method
    private void setScores(GradeValues gv, int coveredBranches, int totalBranches,
                           int detectedMutations, int totalMutations,
                           int metaTestsPassed, int totalMetaTests,
                           int checksPassed, int totalChecks) {
        gv.setBranchGrade(coveredBranches, totalBranches);
        gv.setMutationGrade(detectedMutations, totalMutations);
        gv.setMetaGrade(metaTestsPassed, totalMetaTests);
        gv.setCheckGrade(checksPassed, totalChecks);
    }



    // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*1 = 1.0 --> 100
    @Test
    void equalWeightsFullScores() {
        setScores(gradeValues1, 25, 25,
                55, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(100);
    }

    // 0.25*(22/25) + 0.25*(45/55) + 0.25*(82/100) +  0.25*(4/5) = 0.8295... --> 83
    @Test
    void equalWeightsNotFullScores() {
        setScores(gradeValues1, 22, 25,
                45, 55, 82, 100, 4, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(83);
    }

    // 0.25*(22/25) + 0.25*1 + 0.25*1 +  0.25*1 = 0.97 --> 97
    @Test
    void equalWeightsNotFullBranchCoverage() {
        setScores(gradeValues1, 22, 25,
                55, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(97);
    }

    // 0.25*1 + 0.25*(45/55) + 0.25*1 +  0.25*1 = 0.9545... --> 95
    @Test
    void equalWeightsNotFullMutationCoverage() {
        setScores(gradeValues1, 25, 25,
                45, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(95);
    }

    // 0.25*1 + 0.25*1 + 0.25*(35/100) +  0.25*1 = 0.8375 --> 84
    @Test
    void equalWeightsNotFullSpecScore() {
        setScores(gradeValues1, 25, 25,
                55, 55, 35, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(84);
    }

    // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*(2/5) = 0.85 --> 85
    @Test
    void equalWeightsNotFullCheckScore() {
        setScores(gradeValues1, 25, 25,
                55, 55, 100, 100, 2, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(85);
    }

    // 0.25*1 + 0.25*(50/55) + 0.25*1 +  0.25*1 = 0.9773... --> 98
    @Test
    void failureShouldNotGive0() {
        setScores(gradeValues1, 25, 25,
                50, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        gradeCalculator1.failed();
        assertThat(finalGrade).isEqualTo(98);
        assertThat(gradeCalculator1.isFailed()).isTrue();
    }


    @Test
    void failureShouldGive0() {
        setScores(gradeValues2, 25, 25,
                50, 55, 100, 100, 5, 5);
        gradeCalculator2.failed();
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(0);
        assertThat(gradeCalculator2.isFailed()).isTrue();
    }



    // 0.25*1 + 0.25*1 + 0.25*1 +  0.25*1 = 0.1 --> 100
    @Test
    void zeroBranchesInTotal() {
        setScores(gradeValues1, 0, 0,
                55, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(100);
    }

    // 0.25*1 + 0.25*1 + 0.25*(50/100) +  0.25*1 = 0.875 --> 88
    @Test
    void zeroMutationsInTotal() {
        setScores(gradeValues1, 12, 12,
                0, 0, 50, 100, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(88);
    }

    // 0.25*(24/30) + 0.25*1 + 0.25*1 +  0.25*1 = 0.95 --> 95
    @Test
    void zeroSpecTestsInTotal() {
        setScores(gradeValues1, 24, 30,
                55, 55, 0, 0, 5, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(95);
    }

    // 0.25*(24/30) + 0.25*1 + 0.25*1 +  0.25*1 = 0.95 --> 95
    @Test
    void zeroChecksInTotal() {
        setScores(gradeValues1, 24, 30,
                55, 55, 100, 100, 0, 0);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(95);
    }

    // 0.4*1 + 0.3*1 + 0.2*1 +  0.1*1 = 1.0 --> 100
    @Test
    void differentWeightsFullScores() {
        setScores(gradeValues2, 25, 25,
                55, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(100);
    }

    // 0.4*(22/25) + 0.3*(45/55) + 0.2*(82/100) +  0.1*(4/5) = 0.8415... --> 84
    @Test
    void differentWeightsNotFullScores() {
        setScores(gradeValues2, 22, 25,
                45, 55, 82, 100, 4, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(84);
    }

    // 0.4*(13/25) + 0.3*1 + 0.2*1 +  0.1*1 = 0.808 --> 81
    @Test
    void differentWeightsNotFullBranchCoverage() {
        setScores(gradeValues2, 13, 25,
                55, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(81);
    }

    // 0.4*1 + 0.3*(45/55) + 0.2*1 +  0.1*1 = 0.9454... --> 95
    @Test
    void differentWeightsNotFullMutationCoverage() {
        setScores(gradeValues2, 25, 25,
                45, 55, 100, 100, 5, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(95);
    }

    // 0.4*1 + 0.3*1 + 0.2*(35/100) +  0.1*1 = 0.87 --> 87
    @Test
    void differentWeightsNotFullSpecScore() {
        setScores(gradeValues2, 25, 25,
                55, 55, 35, 100, 5, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(87);
    }

    // 0.4*1 + 0.3*1 + 0.2*1 +  0.1*(2/5) = 0.94 --> 94
    @Test
    void differentWeightsNotFullCheckScore() {
        setScores(gradeValues2, 25, 25,
                55, 55, 100, 100, 2, 5);
        int finalGrade = gradeCalculator2.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(94);
    }


    @Test
    void noScores() {
        setScores(gradeValues1, 0, 25,
                0, 55, 0, 100, 0, 5);
        int finalGrade = gradeCalculator1.calculateFinalGrade();
        assertThat(finalGrade).isEqualTo(0);
    }














}
