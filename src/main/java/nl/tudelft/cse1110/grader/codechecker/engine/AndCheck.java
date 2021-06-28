package nl.tudelft.cse1110.grader.codechecker.engine;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.List;
import java.util.Objects;

public class AndCheck extends CheckType {

    private List<SingleCheck> checks;
    private boolean hasExecuted = false;

    public AndCheck(int weight, String description, List<SingleCheck> checks) {
        super(weight, description);
        this.checks = checks;
    }

    @Deprecated // Jackson only
    public AndCheck() {
        super(1, null);
    }

    @Override
    public boolean getFinalResult() {
        assert hasExecuted : "You have to run the check first";

        return checks.stream().allMatch(c -> c.getFinalResult() == true);
    }

    @Override
    public void runCheck(CompilationUnit cu) {
        for (SingleCheck singleCheck : checks) {
            singleCheck.runCheck(cu);
        }

        hasExecuted = true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndCheck andCheck = (AndCheck) o;
        return Objects.equals(checks, andCheck.checks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checks);
    }
}
