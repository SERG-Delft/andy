package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.ConditionalExpression;

public class ConditionalExpressionUsed extends Check {
    private boolean conditionalExpressionFound = false;

    @Override
    public boolean visit(ConditionalExpression node) {
        conditionalExpressionFound = true;
        return super.visit(node);
    }

    @Override
    public boolean result() {
        return conditionalExpressionFound;
    }

    public String toString() {
        return "Conditional/ternary expression is used";
    }
}
