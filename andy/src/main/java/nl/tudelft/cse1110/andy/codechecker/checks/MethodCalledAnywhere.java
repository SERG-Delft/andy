package nl.tudelft.cse1110.andy.codechecker.checks;

public class MethodCalledAnywhere extends MethodCalledInTestMethod {
    public MethodCalledAnywhere(String methodToBeCalled) {
        super(methodToBeCalled);
    }

    public MethodCalledAnywhere(String expression, String methodToBeCalled) {
        super(expression, methodToBeCalled);
    }

    @Override
    protected boolean isInTheAnnotatedMethod(){
        return true;
    }
}
