package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodCalledAnywhere extends Check {
    private final String methodToBeCalled;
    private boolean methodWasCalled = false;

    public MethodCalledAnywhere(String methodToBeCalled) {
        this.methodToBeCalled = methodToBeCalled;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        String methodName = mi.getName().toString();

        if (methodToBeCalled.equals(methodName))
            methodWasCalled = true;

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