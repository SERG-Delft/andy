package nl.tudelft.cse1110.andy.execution.metatest;

import nl.tudelft.cse1110.andy.result.TestFailureInfo;

import java.util.List;

public class MetaTestReport {

    private int testsRan;
    private int testsFound;
    private int testsSucceeded;

    private List<TestFailureInfo> testsTriggered;

    public MetaTestReport(int testsRan, int testsSucceeded, int testsFound,  List<TestFailureInfo> testsTriggered) {
        this.testsRan = testsRan;
        this.testsSucceeded = testsSucceeded;
        this.testsFound = testsFound;
        this.testsTriggered = testsTriggered;
    }

    public boolean passesTheMetaTest() {
        return testsSucceeded < testsRan;
    }

    public int getTestsRan() {
        return testsRan;
    }

    public int getTestsFound() {
        return testsFound;
    }

    public int getTestsSucceeded() {
        return testsSucceeded;
    }

    public List<TestFailureInfo> getTestsTriggered() {
        return testsTriggered;
    }

    public void setTestsTriggered(List<TestFailureInfo> testsTriggered) {
        this.testsTriggered = testsTriggered;
    }
}
