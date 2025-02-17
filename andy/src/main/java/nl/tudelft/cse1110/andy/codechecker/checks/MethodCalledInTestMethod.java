package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks whether a method was invoked in a test method.
 *
 * Parameter:
 * - the name of the method
 *
 * Output:
 * - true if the method was invoked at least once in any test method.
 *
 */
public class MethodCalledInTestMethod extends WithinTestMethod {

    private final String expression;
    private final String methodToBeCalled;
    private boolean methodWasCalled = false;

    public MethodCalledInTestMethod(String methodToBeCalled) {
        this(null, methodToBeCalled);
    }

    /**
     * This constructor allows you to find nodes such as `Arbitraries.of(1, 2)`:
     * the part before the dot (`Arbitraries`) is the expression, the part after it (`of`) is the methodToBeCalled.
     * Note that we do not check the type of the expression, only the literal syntax.
     * (It should theoretically be possible to find the type binding for the expression, but JDT
     * does not seem to allow finding bindings for nested expressions like in `user.name.toString()`,
     * so adding this would have limited value.)
     */
    public MethodCalledInTestMethod(String expression, String methodToBeCalled) {
        this.expression = expression;
        this.methodToBeCalled = methodToBeCalled;
    }

    @Override
    public boolean visit(MethodInvocation mi) {
        if(isInTheAnnotatedMethod()) {
            String methodName = mi.getName().toString();

            if (expression == null
                    || mi.getExpression() == null
                    || expression.equals(mi.getExpression().toString())) {
                if (methodToBeCalled.equals(methodName))
                    methodWasCalled = true;
            }
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return methodWasCalled;
    }

    public String toString() {
        return methodToBeCalled + " method is called";
    }
}
