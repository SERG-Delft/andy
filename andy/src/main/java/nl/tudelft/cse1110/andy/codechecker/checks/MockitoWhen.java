package nl.tudelft.cse1110.andy.codechecker.checks;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Checks for calls to Mockito's when(), for a specific method name
 * and a specific number of times.
 *
 * Parameters:
 * - The name of the method that when should happen (just the method name, e.g., "add")
 * - The number of expected times the when() should be called.
 *
 * Output: returns true if when() is called for the specific method, the specific
 * number of times.
 */
public class MockitoWhen extends Check {

    private final String methodToSetUp;
    private final int expectedNumberOfOccurrences;
    private final Comparison comparison;

    private int numberOfCallsToWhen = 0;
    private boolean inWhenMode = false;
    private String previousMethodName = "";

    public MockitoWhen(String methodToSetUp, Comparison comparison, int expectedNumberOfOccurrences) {
        this.methodToSetUp = methodToSetUp;
        this.comparison = comparison;
        this.expectedNumberOfOccurrences = expectedNumberOfOccurrences;
    }

    @Override
    public boolean visit(MethodInvocation mi) {

        String methodName = mi.getName().toString();
        /**
         * As soon as a Mockito's when happen, we wait for the next method call.
         * We know that a when() method was just called because of the inWhenMode variable flag.
         *
         * If the method call just after when() matches the one we are expecting, bingo!
         */
        if(inWhenMode) {
            if(methodToSetUp.equals(methodName) || methodToSetUp.equals(previousMethodName)) {
                // With the normal syntax, if we encounter a match in method name,
                // it will be right after when(...).
                // Example: when(mockedList.add("2")).thenReturn(true);
                // the order in which they will be processed is thenReturn(...) -> when(...) -> add(...)
                // With the other syntax we can encounter a match either before when or after it
                // Example: doNothing().when(mockedList).someVoidMethod()
                // the order will be someVoidMethod() -> when(...) -> doNothing()
                // Alternatively, we can also write doReturn(true).when(mockedList.add("1"))
                // the order will be when(...) -> doReturn(...) -> add(...)
                // So we check if we encounter match on the current command or on the previous
                numberOfCallsToWhen++;
                inWhenMode = false;
                previousMethodName = "";
            } else if(previousMethodName.contains("do") && !methodName.equals("when")){
                // If we did not encounter a match, and we are at a do* construct
                // it means that we are going to the next statement and if it starts with "when",
                // we do not reset inWhenMode. We clear previous to avoid potential bugs
                inWhenMode = false;
                previousMethodName = "";
            }  else if(!methodName.contains("do") && !methodName.equals("when")){
                // if we have encountered neither a do* construct, nor when, it means
                // that the statement that is tested simple does not match our desired methodName.
                // So we return from inWhenMode
                inWhenMode = false;
                previousMethodName = "";
            } else {
                previousMethodName = methodName;
            }
        } else {
            /**
             * We wait for a call to when().
             */
            boolean mockitoWhenCalled = "when".equals(methodName);
            if (mockitoWhenCalled) {
                inWhenMode = true;
            } else {
                // We only memorize the previous, if it is not a "when",
                // because if we encounter our matching methodName, before reaching when mode
                // we will miss it
                // Example: doNothing().when(mockedList).someVoidMethod()
                // As stated above if we memorize every previous element,
                // after reaching when mode we would lose someVoidMethod()
                previousMethodName = methodName;
            }
        }
        return super.visit(mi);
    }


    @Override
    public boolean result() {
        return comparison.compare(numberOfCallsToWhen, expectedNumberOfOccurrences);
    }

    public String toString() {
        return methodToSetUp + " is stubbed" + (expectedNumberOfOccurrences > 0 ? " (" + comparison + " " + expectedNumberOfOccurrences + ")" : "");
    }
}
