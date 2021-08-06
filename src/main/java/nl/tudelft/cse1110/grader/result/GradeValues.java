package nl.tudelft.cse1110.grader.result;

public class GradeValues {

    //  flag to indicate whether having test failures should give a 0 right away to the student or not
    private boolean failureGives0;

    // together all weights in [0,1] add up to 1
    private float branchCoverageWeight;
    private float mutationCoverageWeight;
    private float metaTestsWeight;
    private float codeChecksWeight;

    private int coveredBranches;
    private int totalBranches;

    private int detectedMutations;
    private int totalMutations;

    private int metaTestsPassed;
    private int totalMetaTests;

    private int checksPassed;
    private int totalChecks;

    public GradeValues(boolean failureGives0,
                       float branchCoverageWeight, float mutationCoverageWeight, float metaTestsWeight, float codeChecksWeight) {
        this.failureGives0 = failureGives0;
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.metaTestsWeight = metaTestsWeight;
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

    public float getMetaTestsWeight() {
        return metaTestsWeight;
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

    public int getMetaTestsPassed() {
        return metaTestsPassed;
    }

    public int getTotalMetaTests() {
        return totalMetaTests;
    }

    public int getChecksPassed() {
        return checksPassed;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public void setBranchGrade(int coveredBranches, int totalBranches) {
        this.coveredBranches = coveredBranches;
        this.totalBranches = totalBranches;
    }

    public void setMutationGrade(int detectedMutations, int totalMutations) {
        this.detectedMutations = detectedMutations;
        this.totalMutations = totalMutations;
    }

    public void setMetaGrade(int metaTestsPassed, int totalMetaTests) {
        this.metaTestsPassed = metaTestsPassed;
        this.totalMetaTests = totalMetaTests;
    }

    public void setCheckGrade(int checksPassed, int totalChecks) {
        this.checksPassed = checksPassed;
        this.totalChecks = totalChecks;
    }

}