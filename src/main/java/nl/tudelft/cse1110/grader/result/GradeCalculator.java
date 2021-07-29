package nl.tudelft.cse1110.grader.result;

//TODO: we wanna write lots of unit tests for this separate class! (so we make it depend on ints only)

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

public class GradeCalculator {

    private GradeValues gradeValues;
    private boolean failed;


    public GradeCalculator(GradeValues gradeValues) {
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

        float finalDecimalGrade = branchCoverageScore(gradeValues.getCoveredBranches(), gradeValues.getTotalBranches()) * gradeValues.getBranchCoverageWeight()
                + mutationCoverageScore(gradeValues.getDetectedMutations(), gradeValues.getTotalMutations()) * gradeValues.getMutationCoverageWeight()
                + specTestsScore(gradeValues.getSpecTestsPassed(), gradeValues.getTotalSpecTests()) * gradeValues.getSpecTestsWeight()
                + codeChecksScore(gradeValues.getChecksPassed(), gradeValues.getTotalChecks()) * gradeValues.getCodeChecksWeight();

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
     * @param specTestsPassed - no. of spec tests passed
     * @param totalSpecTests - total no. of spec tests
     * @return score based on spec tests
     */
    // TODO: to be implemented
    public float specTestsScore(int specTestsPassed, int totalSpecTests) {
        if (totalSpecTests == 0) {
            return 1f;   // full points assigned
        }
        return (float)specTestsPassed / totalSpecTests;
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
