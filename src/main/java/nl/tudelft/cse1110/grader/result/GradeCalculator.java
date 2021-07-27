package nl.tudelft.cse1110.grader.result;

//TODO: we wanna write lots of unit tests for this separate class!

//TODO: let ResultBuilder use this class, to output final grade to student

public class GradeCalculator {

    //  flag to indicate whether having test failures should give a 0 right away to the student or not
    private boolean failureGives0;

    // together all weights in [0,1] add up to 1
    private float branchCoverageWeight;
    private float mutationCoverageWeight;
    private float specTestsWeight;
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







}
