package nl.tudelft.cse1110.grader.result;

public class GradeValues {

    //  flag to indicate whether having test failures should give a 0 right away to the student or not
    private boolean failureGives0;

    // together all weights in [0,1] add up to 1
    private float branchCoverageWeight;
    private float mutationCoverageWeight;
    private float specTestsWeight;                    // we will pass 0 for now when running our program
    private float codeChecksWeight;

    private int coveredBranches;
    private int totalBranches;

    private int detectedMutations;
    private int totalMutations;

    private int specTestsPassed;
    private int totalSpecTests;

    private int checksPassed;
    private int totalChecks;

    public GradeValues(boolean failureGives0,
                       float branchCoverageWeight, float mutationCoverageWeight, float specTestsWeight, float codeChecksWeight) {
        this.failureGives0 = failureGives0;
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.specTestsWeight = specTestsWeight;
        this.codeChecksWeight = codeChecksWeight;
    }


    public boolean getFailureGives0() {
        return failureGives0;
    }

    public float getBranchCoverageWeight() {
        return branchCoverageWeight;
    }

    public float getMutationCoverageWeight() {
        return mutationCoverageWeight;
    }

    public float getSpecTestsWeight() {
        return specTestsWeight;
    }

    public float getCodeChecksWeight() {
        return codeChecksWeight;
    }

    public int getCoveredBranches() {
        return coveredBranches;
    }

    public int getTotalBranches() {
        return totalBranches;
    }

    public int getDetectedMutations() {
        return detectedMutations;
    }

    public int getTotalMutations() {
        return totalMutations;
    }

    public int getSpecTestsPassed() {
        return specTestsPassed;
    }

    public int getTotalSpecTests() {
        return totalSpecTests;
    }

    public int getChecksPassed() {
        return checksPassed;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public void setCoveredBranches(int coveredBranches) {
        this.coveredBranches = coveredBranches;
    }

    public void setTotalBranches(int totalBranches) {
        this.totalBranches = totalBranches;
    }

    public void setDetectedMutations(int detectedMutations) {
        this.detectedMutations = detectedMutations;
    }

    public void setTotalMutations(int totalMutations) {
        this.totalMutations = totalMutations;
    }

    public void setSpecTestsPassed(int specTestsPassed) {
        this.specTestsPassed = specTestsPassed;
    }

    public void setTotalSpecTests(int totalSpecTests) {
        this.totalSpecTests = totalSpecTests;
    }

    public void setChecksPassed(int checksPassed) {
        this.checksPassed = checksPassed;
    }

    public void setTotalChecks(int totalChecks) {
        this.totalChecks = totalChecks;
    }
}
