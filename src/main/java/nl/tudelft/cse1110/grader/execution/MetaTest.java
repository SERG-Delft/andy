package nl.tudelft.cse1110.grader.execution;

public class MetaTest {

    private String old;
    private String replacement;
    private String name;

    public MetaTest(String name, String old, String replacement) {
        this.old = old;
        this.replacement = replacement;
        this.name = name;
    }

    public String getOld() {
        return old;
    }

    public String getReplacement() {
        return replacement;
    }

    public String getName() {
        return name;
    }
}
