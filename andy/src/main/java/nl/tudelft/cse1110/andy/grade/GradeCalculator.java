package nl.tudelft.cse1110.andy.grade;

public class GradeCalculator {

    /*
     * Calculates the final grade of the student, in a number between 0 and 100.
     * Final number is rounded up from 0.5.
     */
    public int calculateFinalGrade(GradeValues gradeValues, GradeWeight weights) {

        this.checkNonzeroWeightValidity(gradeValues, weights);

        float branchScore = branchCoverageScore(gradeValues.getCoveredBranches(), gradeValues.getTotalBranches())
                * weights.getBranchCoverageWeight();
        float mutationScore = mutationCoverageScore(gradeValues.getDetectedMutations(), gradeValues.getTotalMutations())
                * weights.getMutationCoverageWeight();
        float metaScore = metaTestsScore(gradeValues.getMetaTestsPassed(), gradeValues.getTotalMetaTests())
                * weights.getMetaTestsWeight();
        float checkScore = codeChecksScore(gradeValues.getChecksPassed(), gradeValues.getTotalChecks())
                * weights.getCodeChecksWeight();

        float finalDecimalGrade = branchScore + mutationScore + metaScore + checkScore;

        int finalGrade = Math.round(finalDecimalGrade * 100);

        if(finalGrade < 0 || finalGrade > 100)
            throw new RuntimeException("Invalid grade calculation");

        if(gradeValues.getPenalty() < 0){
            throw new RuntimeException("Negative penalty: " + gradeValues.getPenalty());
        }

        // Apply penalty
        finalGrade -= gradeValues.getPenalty();

        // Grade should not go below 0
        // The total penalty can be more than 100: for example, if there are two failing code checks
        //  which both have a penalty of 100, the total penalty will be 200, and the final grade should be 0.
        if(finalGrade < 0) finalGrade = 0;

        // Grades between 99.5 and 100 should be rounded down to 99 instead of up
        if (finalGrade == 100 && hasIncompleteComponents(gradeValues, weights)) {
            finalGrade = 99;
        }

        return finalGrade;
    }


    /**
     * @param coveredBranches - no. of branches covered by test class
     * @param totalBranches - total no. of branches in source code under test
     * @return branch coverage score between 0 and 1
     */
    public float branchCoverageScore(int coveredBranches, int totalBranches) {
        if (totalBranches == 0) {
            return 1f;   // full points assigned
        }
        return (float)coveredBranches / totalBranches;
    }


    /**
     * @param detectedMutations - no. of mutants killed
     * @param totalMutations - total no. of mutants generated
     * @return mutation coverage score between 0 and 1
     */
    public float mutationCoverageScore(int detectedMutations, int totalMutations) {
        if (totalMutations == 0) {
            return 1f;   // full points assigned
        }
        return (float)detectedMutations / totalMutations;
    }


    /**
     * @param metaTestsPassed - no. of meta tests passed
     * @param totalMetaTests - total no. of meta tests
     * @return score based on spec tests
     */
    public float metaTestsScore(int metaTestsPassed, int totalMetaTests) {
        if (totalMetaTests == 0) {
            return 1f;   // full points assigned
        }
        return (float)metaTestsPassed / totalMetaTests;
    }


    /**
     * @param checksPassed - no. of checks passed
     * @param totalChecks - total no. of checks
     * @return score based on code checks
     */
    public float codeChecksScore(int checksPassed, int totalChecks) {
        if (totalChecks == 0) {
            return 1f;   // full points assigned
        }
        return (float)checksPassed / totalChecks;
    }

    /**
     * @param gradeValues the grade values
     * @param weights     the weights
     * @return whether any of the components with positive weight are incomplete
     */
    private boolean hasIncompleteComponents(GradeValues gradeValues, GradeWeight weights) {
        return weights.getBranchCoverageWeight() > 0.0f && gradeValues.getCoveredBranches() < gradeValues.getTotalBranches() ||
               weights.getMutationCoverageWeight() > 0.0f && gradeValues.getDetectedMutations() < gradeValues.getTotalMutations() ||
               weights.getMetaTestsWeight() > 0.0f && gradeValues.getMetaTestsPassed() < gradeValues.getTotalMetaTests() ||
               weights.getCodeChecksWeight() > 0.0f && gradeValues.getChecksPassed() < gradeValues.getTotalChecks();
    }


    /**
     * @param gradeValues the grade values
     * @param weights     the weights
     * @throws RuntimeException if any of the components with positive weight have a maximum score of 0
     */
    private void checkNonzeroWeightValidity(GradeValues gradeValues, GradeWeight weights) throws RuntimeException {
        if (weights.getCodeChecksWeight() > 0 && gradeValues.getTotalChecks() == 0)
            throw new RuntimeException("There are 0 code checks but the code check weight is not 0");

        if (weights.getBranchCoverageWeight() > 0 && gradeValues.getTotalBranches() == 0)
            throw new RuntimeException("There are 0 branches to cover (or jacoco is disabled) but the " +
                                       "branch coverage weight is not 0");

        if (weights.getMetaTestsWeight() > 0 && gradeValues.getTotalMetaTests() == 0)
            throw new RuntimeException("There are 0 meta tests but the meta test weight is not 0");

        if (weights.getMutationCoverageWeight() > 0 && gradeValues.getTotalMutations() == 0)
            throw new RuntimeException("There are 0 mutants (or pitest failed or is disabled) but the " +
                                       "mutation coverage weight is not 0");
    }

}
