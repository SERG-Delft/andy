package nl.tudelft.cse1110.grader.result;

//TODO: we wanna write lots of unit tests for this separate class!

import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

public class GradeCalculator {

    //  flag to indicate whether having test failures should give a 0 right away to the student or not
    private boolean failureGives0;

    // together all weights in [0,1] add up to 1
    private float branchCoverageWeight;
    private float mutationCoverageWeight;
    private float specTestsWeight;          // we will pass 0 for now when running our program
    private float codeChecksWeight;


    public GradeCalculator(boolean failureGives0,
                           float branchCoverageWeight, float mutationCoverageWeight,
                           float specTestsWeight, float codeChecksWeight) {

        this.failureGives0 = failureGives0;
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.specTestsWeight = specTestsWeight;
        this.codeChecksWeight = codeChecksWeight;
    }


    //TODO: let ResultBuilder call this method, to output final grade to student
    public float calculateFinalGrade() {
        // if flag on and any test failure detected, return 0.
        // call helper boolean testFailureFound (see below)
        return 0;

        // use all 4 scores to return final grade
    }


    public float branchCoverageScore() {

        // Step RunJacoco passes float branchesCovered and float totalBranches in execute(), or the total score if accessible

        // score * weight
        return 0;
    }


    public float mutationCoverageScore(CombinedStatistics stats) {

        // Step RunPiTest passes CombinedStatistics stats in execute()
        // we use stats.getMutationStatistics().getTotalDetectedMutations() / stats.getMutationStatistics().getTotalMutations()

        // score * weight
        return 0;
    }


    // TODO: let ResultBuilder pass the corresponding parameters ?
    public float specTestsScore(int specTestsCovered, int totalSpecTests) {

        // we will pass 0 for now when running our program

        // score * weight
        return 0;
    }


    public float codeChecksScore() {

        // option 1: make weightedChecks() and weights() in CheckScript public,
        //           so CodeChecksStep can call it and pass weightScore / totalWeight to this method

        // option 2: CodeChecksStep passes script in execute() and we mimic the above 2 private methods here, by calling script.getChecks()

        // option 3: CodeChecksStep passes script in execute() and we call script.generateReport()
        //          which returns a string that we'll have to parse

        // score * weight
        return 0;
    }



    public boolean testFailureFound(TestExecutionSummary summary) {

        // RunJUnitTests passes summary in execute()

        // return summary.getTestsFoundCount() - summary.getTestsSucceededCount() > 0

        return false;

    }










}
