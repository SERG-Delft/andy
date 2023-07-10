package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.*;

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

    /**
     * Checks for class instance creations to
     * check if the instantiated class if one of the classes
     * we are looking for
     */
    @Override
    public boolean visit(ClassInstanceCreation cic) {
        String className = cic.getType().toString();
        if (classToBeUsed.equals(className))
            classWasUsed = true;

        return super.visit(cic);
    }

    @Override
    public boolean visit(MethodInvocation mi) {
        Expression expression = mi.getExpression();
        if (expression != null) {
            if (expression.toString().equals(classToBeUsed))
                classWasUsed = true;


        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return classWasUsed;
    }

    public String toString() {
        return classToBeUsed + " class is used";
    }
}
