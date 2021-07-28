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


    // ResultBuilder calls this method, to output final grade to student
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


    public float branchCoverageScore(int coveredBranches, int totalBranches) {
        return coveredBranches / totalBranches;
    }


    public float mutationCoverageScore(int detectedMutations, int totalMutations) {
        return detectedMutations / totalMutations;
    }


    // TODO: to be implemented, ResultBuilder will pass 100/100 for now when running our program
    public float specTestsScore(int specTestsPassed, int totalSpecTests) {
        return specTestsPassed / totalSpecTests;
    }


    public float codeChecksScore(int checksPassed, int totalChecks) {
        return checksPassed/totalChecks;
    }


    public void failed() {
        failed = true;
    }


}
