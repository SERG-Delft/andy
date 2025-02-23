package nl.tudelft.cse1110.andy.codechecker.checks;

import java.util.Set;

public class MethodCalledInProvideMethod extends MethodCalledInTestMethod {
    public MethodCalledInProvideMethod(String methodToBeCalled) {
        super(methodToBeCalled);
    }

    public MethodCalledInProvideMethod(String expression, String methodToBeCalled) {
        super(expression, methodToBeCalled);
    }

    @Override
    protected Set<String> annotations() {
        return Set.of("Provide");
    }
}
