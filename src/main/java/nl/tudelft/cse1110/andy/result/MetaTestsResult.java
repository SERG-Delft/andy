package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class MetaTestsResult {
    private final boolean wasExecuted;
    private final int score;
    private final int totalTests;
    private final List<String> passes;
    private final List<String> failures;

    private MetaTestsResult(boolean wasExecuted, int score, int totalTests, List<String> passes, List<String> failures) {
        this.wasExecuted = wasExecuted;
        this.score = score;
        this.totalTests = totalTests;
        this.passes = passes;
        this.failures = failures;

        if(this.passes.size() + this.failures.size() > this.totalTests)
            throw new RuntimeException("Number of meta tests do not match. Please, contact the teacher");

        if(this.score > this.totalTests)
            throw new RuntimeException("Meta tests score greater than maximum. Please, contact the teacher");
    }

    public static MetaTestsResult build(int score, int totalTests, List<String> passes, List<String> failures) {
        return new MetaTestsResult(true, score, totalTests, passes, failures);
    }

    public static MetaTestsResult empty() {
        return new MetaTestsResult(false, 0, 0, Collections.emptyList(), Collections.emptyList());
    }

    public int getPassedMetaTests() {
        return score;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public List<String> getPasses() {
        return Collections.unmodifiableList(passes);
    }

    public List<String> getFailures() {
        return Collections.unmodifiableList(failures);
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }
}
