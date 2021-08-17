package nl.tudelft.cse1110.codechecker.engine;

import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.List;
import java.util.Objects;

public class OrCheck extends CheckType {

    private List<CheckType> checks;
    private boolean hasExecuted = false;

    public OrCheck(int weight, String description, List<CheckType> checks) {
        super(weight, description);
        this.checks = checks;
    }

    @Override
    public boolean getFinalResult() {
        assert hasExecuted : "You have to run the check first";

        return checks.stream().anyMatch(CheckType::getFinalResult);
    }

    @Override
    public void runCheck(CompilationUnit cu) {
        for (CheckType singleCheck : checks) {
            singleCheck.runCheck(cu);
        }

        hasExecuted = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrCheck orCheck = (OrCheck) o;
        return Objects.equals(checks, orCheck.checks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checks);
    }
}
