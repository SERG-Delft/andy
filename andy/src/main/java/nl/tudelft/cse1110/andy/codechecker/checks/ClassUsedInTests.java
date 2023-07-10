package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether a class is used - instantiated or one of its methods called.
 * Parameter:
 * - the name of the method
 * Output:
 * - true if the class was used.
 *
 */
public class ClassUsedInTests extends WithinTestMethod {

    private final String classToBeUsed;
    private boolean classWasUsed = false;

    public ClassUsedInTests(String classToBeUsed) {
        this.classToBeUsed = classToBeUsed;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        if(isInTheAnnotatedMethod()) {
            String className = mi.getClass().toString();

            if (classToBeUsed.equals(className))
                classWasUsed = true;
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return classWasUsed;
    }

    public String toString() {
        return classToBeUsed + " class is called";
    }
}
