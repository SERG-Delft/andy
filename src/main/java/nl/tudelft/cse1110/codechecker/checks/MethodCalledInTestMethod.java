package nl.tudelft.cse1110.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.List;

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

    private final String methodToBeCalled;
    private boolean methodWasCalled = false;

    public MethodCalledInTestMethod(List<String> params) {
        assert params!=null;
        assert params.size() == 1;

        this.methodToBeCalled = params.get(0);
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        if(isInTheAnnotatedMethod()) {
            String methodName = mi.getName().toString();

            if (methodToBeCalled.equals(methodName))
                methodWasCalled = true;
        }

        return super.visit(mi);
    }

    @Override
    public boolean result() {
        return methodWasCalled;
    }
}
