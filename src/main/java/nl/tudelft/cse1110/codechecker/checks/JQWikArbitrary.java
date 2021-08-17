package nl.tudelft.cse1110.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Checks whether a specific Arbitrary is defined.
 * Looks for method returns of Arbitrary<X>
 *
 * Parameters:
 * - the type to look for. Empty means any type.
 *
 * Output:
 * - true if a method returns it at least once
 */
public class JQWikArbitrary extends Check {

    private final String type;
    private final boolean specificType;
    private boolean arbitraryIsReturned = false;

    public JQWikArbitrary(String type) {
        this.type = type;
        this.specificType = true;
    }

    public JQWikArbitrary() {
        this.type = null;
        this.specificType = false;
    }

    @Override
    public boolean visit(MethodDeclaration mi) {

        if(mi.getReturnType2()!=null) {
            String returnType = mi.getReturnType2().toString();

            if(specificType && String.format("Arbitrary<%s>", type).equals(returnType)) {
                arbitraryIsReturned = true;
            } else if (!specificType && returnType.startsWith("Arbitrary<")){
                arbitraryIsReturned = true;
            }
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return arbitraryIsReturned;
    }
}
