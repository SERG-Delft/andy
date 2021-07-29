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


    /** ResultBuilder calls this method, to output final grade to student
     * @return - final grade between 0 and 1
     *   * In logFinalGrade(), we round up from 0.5 to output in format 85/100 e.g.
     */
    public float calculateFinalGrade() {

        if (gradeValues.getFailureGives0() && failed) {
            return 0;
        }

        float finalGrade = branchCoverageScore(gradeValues.getCoveredBranches(), gradeValues.getTotalBranches()) * gradeValues.getBranchCoverageWeight()
                + mutationCoverageScore(gradeValues.getDetectedMutations(), gradeValues.getTotalMutations()) * gradeValues.getMutationCoverageWeight()
                + specTestsScore(gradeValues.getSpecTestsPassed(), gradeValues.getTotalSpecTests()) * gradeValues.getSpecTestsWeight()
                + codeChecksScore(gradeValues.getChecksPassed(), gradeValues.getTotalChecks()) * gradeValues.getCodeChecksWeight();

        return finalGrade;
    }


    /**
     * @param coveredBranches - no. of branches covered by test class
     * @param totalBranches - total no. of branches in source code under test
     * @return branch coverage score between 0 and 1
     */
    public float branchCoverageScore(int coveredBranches, int totalBranches) {
        try {
            return (float)coveredBranches / totalBranches;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            System.out.println("Total number of branches configured is 0!");
            return 0;
        }
    }


    /**
     * @param detectedMutations - no. of mutants killed
     * @param totalMutations - total no. of mutants generated
     * @return mutation coverage score between 0 and 1
     */
    public float mutationCoverageScore(int detectedMutations, int totalMutations) {
        try {
            return (float)detectedMutations / totalMutations;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            System.out.println("Total number of mutations configured is 0!");
            return 0;
        }
    }


    /**
     * @param specTestsPassed - no. of spec tests passed
     * @param totalSpecTests - total no. of spec tests
     * @return score based on spec tests
     */
    // TODO: to be implemented, ResultBuilder will pass 100/100 for now when running our program
    public float specTestsScore(int specTestsPassed, int totalSpecTests) {
        try {
            return (float)specTestsPassed / totalSpecTests;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            System.out.println("Total number of spec tests configured is 0!");
            return 0;
        }
    }


    /**
     * @param checksPassed - no. of checks passed
     * @param totalChecks - total no. of checks
     * @return score based on code checks
     */
    public float codeChecksScore(int checksPassed, int totalChecks) {
        try {
            return (float)checksPassed / totalChecks;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            System.out.println("Total number of checks configured is 0!");
            return 0;
        }
    }


    /**
     * If one of the test fails, field "failed" is set to true.
     */
    public void failed() {
        failed = true;
    }


}
