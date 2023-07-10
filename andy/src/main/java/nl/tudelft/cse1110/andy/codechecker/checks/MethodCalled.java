package nl.tudelft.cse1110.andy.codechecker.checks;

public class MethodCalled extends MethodCalledInTestMethod {
    public MethodCalled(String methodToBeCalled) {
        super(methodToBeCalled);
    }

    @Override
    protected boolean isInTheAnnotatedMethod(){
        return true;
    }
}
