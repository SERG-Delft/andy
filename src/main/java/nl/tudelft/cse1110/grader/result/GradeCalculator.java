package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

public class GradeCalculator {

    private GradeValues gradeValues;
    private boolean failed;


    public GradeCalculator() {}

    public GradeCalculator(GradeValues gradeValues) {
        this.gradeValues = gradeValues;
    }

    public void setGradeValues(GradeValues gradeValues) {
        this.gradeValues = gradeValues;
    }

    /** ResultBuilder calls this method in logFinalGrade(), to output final grade to student in format 85/100 e.g.
     * @return - final grade as int between 0 and 100
     *  We round up from 0.5
     */
    public int calculateFinalGrade() {

        if (gradeValues.getFailureGives0() && failed) {
            return 0;
        }

        float branchScore = branchCoverageScore(gradeValues.getCoveredBranches(), gradeValues.getTotalBranches())
                * gradeValues.getBranchCoverageWeight();
        float mutationScore = mutationCoverageScore(gradeValues.getDetectedMutations(), gradeValues.getTotalMutations())
                * gradeValues.getMutationCoverageWeight();
        float metaScore = metaTestsScore(gradeValues.getMetaTestsPassed(), gradeValues.getTotalMetaTests())
                * gradeValues.getMetaTestsWeight();
        float checkScore = codeChecksScore(gradeValues.getChecksPassed(), gradeValues.getTotalChecks())
                * gradeValues.getCodeChecksWeight();

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


    /**
     * If one of the test fails, field "failed" is set to true.
     */
    public void failed() {
        failed = true;
    }

    public boolean isFailed() {
        return failed;
    }


}
