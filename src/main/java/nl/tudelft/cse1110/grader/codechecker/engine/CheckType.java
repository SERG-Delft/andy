package nl.tudelft.cse1110.grader.codechecker.engine;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.eclipse.jdt.core.dom.CompilationUnit;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = SingleCheck.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SingleCheck.class, name = "single"),
        @JsonSubTypes.Type(value = OrCheck.class, name = "or"),
        @JsonSubTypes.Type(value = AndCheck.class, name = "and")
})
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class CheckType {

    protected int weight;
    protected String description;

    public CheckType(int weight, String description) {
        this.weight = weight;
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public abstract boolean getFinalResult();
    public abstract void runCheck(CompilationUnit cu);

    public String reportExecution() {
        return String.format("%s: %s (weight: %d)\n",
                description,
                getFinalResult() ? "PASS" : "FAIL",
                weight);
    }
}
