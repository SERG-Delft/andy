package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class CodeChecksResult {
    private final boolean wasExecuted;
    private final List<CodeCheckResult> checkResults;

    private CodeChecksResult(boolean wasExecuted, List<CodeCheckResult> checkResults) {
        this.wasExecuted = wasExecuted;
        this.checkResults = checkResults;
    }

    public static CodeChecksResult empty() {
        return new CodeChecksResult(false, Collections.emptyList());
    }

    public static CodeChecksResult build(List<CodeCheckResult> checkResults) {
        return new CodeChecksResult(true, checkResults);
    }

    public int getNumberOfPassedChecks() {
        return getNumberOfPassedChecks(true);
    }

    public int getNumberOfPassedChecks(boolean includeWeight) {
        return checkResults.stream().mapToInt(check -> check.passed() ?
                (includeWeight ? check.getWeight() : 1) :
                0).sum();
    }

    public int getTotalNumberOfChecks() {
        return getTotalNumberOfChecks(true);
    }

    public int getTotalNumberOfChecks(boolean includeWeight) {
        return checkResults.stream().mapToInt(c -> (includeWeight ? c.getWeight() : 1)).sum();
    }

    public List<CodeCheckResult> getCheckResults() {
        return Collections.unmodifiableList(checkResults);
    }

    public boolean hasChecks() {
        return !checkResults.isEmpty();
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }

    @Override
    public String toString() {
        return "CodeChecksResult{" +
                "wasExecuted=" + wasExecuted +
                ", checkResults=" + checkResults +
                '}';
    }
}
