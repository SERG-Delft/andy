package nl.tudelft.cse1110.andy.codechecker.checks;

import com.google.common.collect.ImmutableSet;
import org.eclipse.jdt.core.dom.StringLiteral;

import java.util.HashSet;
import java.util.Set;

/**
 * Check whether a test method uses string literals
 *
 * Parameters:
 * - length: length of the string to look for.
 *
 * Output: true if at least a single string with length greater than or
 * equal to length is found; false otherwise.
 */
public class UseOfStringLiterals extends WithinAnnotatedMethod {

    /**
     * in JUnit tests and in JQWik methods
     */
    public static Set<String> TEST_ANNOTATIONS =
            new HashSet<>(ImmutableSet.of(
                "Test", // junit
                "ParameterizedTest", // junit
                "Property", // jqwik
                "Provide" // jqwik
            ));

    private boolean literalFound = false;
    private int length;

    public UseOfStringLiterals(int length) {
        this.length = length;
    }

    protected Set<String> annotations() {
        return TEST_ANNOTATIONS;
    }

    public boolean visit(StringLiteral node) {
        if(isInTheAnnotatedMethod() && node.getLiteralValue().length()>=length)
            literalFound = true;

        return super.visit(node);
    }


    @Override
    public boolean result() {
        return literalFound;
    }

    public String toString() {
        return "Uses string literals";
    }


}
