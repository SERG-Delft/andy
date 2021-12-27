package nl.tudelft.cse1110.andy.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetaTestsResult {
    private boolean wasExecuted;
    private int score;
    private int totalTests;
    private final ArrayList<MetaTestResult> metaTestResults;

        if(this.metaTestResults.size() > this.totalTests)
            throw new RuntimeException("Number of meta tests do not match.");

        if(this.score > this.totalTests)
            throw new RuntimeException("Meta tests score greater than maximum.");
    }

    public static MetaTestsResult build(int score, int totalTests, List<MetaTestResult> metaTestResults) {
        return new MetaTestsResult(true, score, totalTests, metaTestResults);
    }

    public static MetaTestsResult empty() {
        return new MetaTestsResult(false, 0, 0, Collections.emptyList());
    }

    public MetaTestsResult addResults(int score, int totalTests, List<MetaTestResult> metaTestResults) {
        if (metaTestResults.size() > totalTests)
            throw new RuntimeException("Number of meta tests does not match.");

        if (score > totalTests)
            throw new RuntimeException("Meta test score greater than maximum.");

        this.score += score;
        this.totalTests += totalTests;
        this.metaTestResults.addAll(metaTestResults);
        this.wasExecuted = true;

        return this;
    }

    public int getPassedMetaTests() {
        return score;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public List<MetaTestResult> getMetaTestResults() {
        return Collections.unmodifiableList(metaTestResults);
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    @Override
    public String toString() {
        return "MetaTestsResult{" +
                "wasExecuted=" + wasExecuted +
                ", score=" + score +
                ", totalTests=" + totalTests +
                ", metaTestResults=" + metaTestResults +
                '}';
    }
}
