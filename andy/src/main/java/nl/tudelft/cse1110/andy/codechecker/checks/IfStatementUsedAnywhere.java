package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.*;

public class IfStatementUsedAnywhere extends Check {
    private boolean ifStatementFound = false;

    @Override
    public boolean visit(IfStatement node) {
        ifStatementFound = true;
        return super.visit(node);
    }

    @Override
    public boolean result() {
        return ifStatementFound;
    }

    public String toString() {
        return "If-statement is used";
    }
}
