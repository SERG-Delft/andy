package nl.tudelft.cse1110.andy.result;

public class MetaTestResult {
    private final String name;
    private final int weight;
    private final boolean passed;

    public MetaTestResult(String name, int weight, boolean passed) {
        this.name = name;
        this.weight = weight;
        this.passed = passed;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean isPassed() {
        return passed;
    }
}
