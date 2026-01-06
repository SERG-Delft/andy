package nl.tudelft.cse1110.andy.execution.metatest;

public class MetaTestReport {

    private int testsRan;
    private int testsFound;
    private int testsSucceeded;

    public MetaTestReport(int testsRan, int testsSucceeded, int testsFound) {
        this.testsRan = testsRan;
        this.testsSucceeded = testsSucceeded;
        this.testsFound = testsFound;
    }


    public boolean passesTheMetaTest() {
        return testsSucceeded < testsRan;
    }
}
