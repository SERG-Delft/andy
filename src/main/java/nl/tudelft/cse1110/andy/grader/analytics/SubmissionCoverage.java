package nl.tudelft.cse1110.andy.grader.analytics;

public class SubmissionCoverage {

    private final int branchesCovered;
    private final int totalBranches;

    private final int mutationCovered;
    private final int totalMutations;

    public SubmissionCoverage(int branchesCovered, int totalBranches, int mutationCovered, int totalMutations) {
        this.branchesCovered = branchesCovered;
        this.totalBranches = totalBranches;
        this.mutationCovered = mutationCovered;
        this.totalMutations = totalMutations;
    }
}
