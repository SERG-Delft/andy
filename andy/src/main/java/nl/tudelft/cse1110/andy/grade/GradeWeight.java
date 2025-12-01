package nl.tudelft.cse1110.andy.grade;

import java.util.Map;

public class GradeWeight {
    // together all weights in [0,1] add up to 1
    private final float branchCoverageWeight;
    private final float mutationCoverageWeight;
    private final float metaTestsWeight;
    private final float codeChecksWeight;
    private final float qualityWeight;

    public GradeWeight(float branchCoverageWeight, float mutationCoverageWeight, float metaTestsWeight, float codeChecksWeight) {
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.metaTestsWeight = metaTestsWeight;
        this.codeChecksWeight = codeChecksWeight;
        this.qualityWeight = 0.0f; // use alternative constructor for quality check (backward compatibility)

        // weights have to sum up to 1
        float weightSum = branchCoverageWeight + mutationCoverageWeight + metaTestsWeight + codeChecksWeight;
        float epsilon = Math.abs(1 - weightSum);
        if(epsilon > 0.001)
            throw new RuntimeException("The weight configuration is wrong! Call the teacher!");
    }

    /* Alternative constructor for assignments with quality check */
    public GradeWeight(float branchCoverageWeight, float mutationCoverageWeight,  float metaTestsWeight, float codeChecksWeight, float qualityWeight) {
        this.branchCoverageWeight = branchCoverageWeight;
        this.mutationCoverageWeight = mutationCoverageWeight;
        this.metaTestsWeight = metaTestsWeight;
        this.codeChecksWeight = codeChecksWeight;
        this.qualityWeight = qualityWeight;

        // weights have to sum up to 1
        float weightSum = branchCoverageWeight + mutationCoverageWeight + metaTestsWeight + codeChecksWeight + qualityWeight;
        float epsilon = Math.abs(1 - weightSum);
        if(epsilon > 0.001)
            throw new RuntimeException("The weight configuration is wrong! Call the teacher!");
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

    public float getQualityWeight() {
        return qualityWeight;
    }

    public static GradeWeight fromConfig(Map<String, Float> weights) {
        float coverage   = weights.getOrDefault("coverage",   0.0f);
        float mutation   = weights.getOrDefault("mutation",   0.0f);
        float meta       = weights.getOrDefault("meta",       0.0f);
        float codechecks = weights.getOrDefault("codechecks", 0.0f);

        return new GradeWeight(coverage, mutation, meta, codechecks);
    }
}
