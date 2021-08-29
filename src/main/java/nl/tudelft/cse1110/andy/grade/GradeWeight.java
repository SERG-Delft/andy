package nl.tudelft.cse1110.andy.grade;

public class GradeWeight {
    // together all weights in [0,1] add up to 1
    private final boolean failureGives0;
    private final float branchCoverageWeight;
    private final float mutationCoverageWeight;
    private final float metaTestsWeight;
    private final float codeChecksWeight;

    public GradeWeight(boolean failureGives0, float branchCoverageWeight, float mutationCoverageWeight, float metaTestsWeight, float codeChecksWeight) {
        this.failureGives0 = failureGives0;
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.metaTestsWeight = metaTestsWeight;
        this.codeChecksWeight = codeChecksWeight;

        // weights have to sum up to 1
        float weightSum = branchCoverageWeight + mutationCoverageWeight + metaTestsWeight + codeChecksWeight;
        float epsilon = Math.abs(1 - weightSum);
        if(epsilon > 0.001)
            throw new RuntimeException("The weight configuration is wrong! Call the teacher!");
    }

    public boolean isFailureGives0() {
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
}
