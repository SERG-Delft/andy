package nl.tudelft.cse1110.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

import java.util.*;

/**
 * Checks for calls to Mockito's verify(), for a specific method name
 * and a specific number of times.
 *
 * Parameters:
 * - The name of the method to verify (just the method name, e.g., "add")
 * - The method type (TEST or AFTEREACH) to look for the verify() called.
 * - The comparator (LT, LTE, GT, GTE, EQ)
 * - The number of expected times the verify() should be called.
 *
 * Output: returns true if verify() is called for the specific method, the specific
 * number of times.
 */
public class MockitoVerify extends WithinAnnotatedMethod {

    private final String methodToVerify;
    private final int expectedNumberOfOccurrences;
    private final Comparison comparison;
    private final String methodType;

    private final boolean never;

    private int numberOfCallsToVerify = 0;
    private String lastMethodCalled = null;



    private static Map<String, Set<String>> METHOD_TYPES = new HashMap<>() {{
       put("TEST", WithinTestMethod.TEST_ANNOTATIONS);
       put("AFTEREACH", WithinAfterEach.AFTEREACH_ANNOTATION);
    }};
    private boolean waitingForNever;

    public MockitoVerify(List<String> params) {
        assert params!=null;
        assert params.size() == 4 || params.size() == 5;

        this.methodToVerify = params.get(0);
        this.methodType = params.get(1);
        assert METHOD_TYPES.containsKey(this.methodType);

        this.comparison = ComparisonFactory.build(params.get(2));
        this.expectedNumberOfOccurrences = Integer.parseInt(params.get(3));

        this.never = params.size() == 5 ? Boolean.parseBoolean(params.get(4)) : false;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        if(isInTheAnnotatedMethod()) {
            /**
             * JDT visits the methods from the outside in
             * e.g., in verify(a).b(), JDT would first visit b(), and then a().
             * In the case of verify(class, never()).method(), JDT will visit method(), then verify() and then never().
             *
             * Maybe another idea would be to parse mi.toString(), as it will return "verify(a).b()".
             * Not sure which one would work better, but I feel parsing strings is always trickier,
             * and full of cases we can't see...
             */
            String methodName = mi.getName().toString();

            boolean mockitoVerifyCalled = "verify".equals(methodName);
            boolean neverCalled = "never".equals(methodName);
            boolean lastMethodCalledWasTheExpectedOne = methodToVerify.equals(lastMethodCalled);

            if (mockitoVerifyCalled && lastMethodCalledWasTheExpectedOne) {
                if(!never) {
                    numberOfCallsToVerify++;
                }
                else waitingForNever = true;
            } else if(neverCalled && waitingForNever) {
                numberOfCallsToVerify++;
                waitingForNever = false;
            } else {
                lastMethodCalled = methodName;
                waitingForNever = false;
            }
        }

        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return comparison.compare(numberOfCallsToVerify, expectedNumberOfOccurrences);
    }

    @Override
    protected Set<String> annotations() {
        return METHOD_TYPES.get(methodType);
    }
}
