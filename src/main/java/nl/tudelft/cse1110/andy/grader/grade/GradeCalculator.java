package nl.tudelft.cse1110.andy.grader.grade;

public class GradeCalculator {

    private GradeWeight weights;

    public GradeCalculator(GradeWeight weights) {
        this.weights = weights;
    }

    public void setWeights(GradeWeight weights) {
        this.weights = weights;
    }

    /*
     * Calculates the final grade of the student, in a number between 0 and 100.
     * Final number is rounded up from 0.5.
     */
    public int calculateFinalGrade(GradeValues gradeValues, boolean failed) {
        if (weights.isFailureGives0() && failed) {
            return 0;
        }

        float branchScore = branchCoverageScore(gradeValues.getCoveredBranches(), gradeValues.getTotalBranches())
                * weights.getBranchCoverageWeight();
        float mutationScore = mutationCoverageScore(gradeValues.getDetectedMutations(), gradeValues.getTotalMutations())
                * weights.getMutationCoverageWeight();
        float metaScore = metaTestsScore(gradeValues.getMetaTestsPassed(), gradeValues.getTotalMetaTests())
                * weights.getMetaTestsWeight();
        float checkScore = codeChecksScore(gradeValues.getChecksPassed(), gradeValues.getTotalChecks())
                * weights.getCodeChecksWeight();

        float finalDecimalGrade = branchScore + mutationScore + metaScore + checkScore;

        return Math.round(finalDecimalGrade * 100);
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



}
