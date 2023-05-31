Researchers at the Aerospace Engineering faculty at TU Delft want to detect and research interesting celestial objects that emit certain signals. To do that, they have recorded sequences of signals from various objects and have classified each signal in one of three categories: *ALPHA*, *BETA*, and *GAMMA*.
As they have collected a lot of data, they have made a program to check whether each signal sequence is relevant for their research. You can see this program in the library tab.

The method `numberOfOccurrences` counts the number of occurrences of the pattern **_ALPHA, GAMMA, BETA, GAMMA, BETA_** in the sequence of signals.

If the number of occurrences is at least **3**, the sequence is considered relevant and the number of occurrences is returned.

If the number of occurrences is less than **3**, the sequence is considered irrelevant and `-1` is returned.

For example:

- For the sequence "BETA, ALPHA, BETA, BETA, ALPHA, BETA, GAMMA", the method would return `-1`, as the number of occurrences (0) of the pattern is less than 3.
- For the sequence "BETA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, GAMMA, ALPHA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, BETA", the method would return `-1`, as the number of occurrences (2) of the pattern is less than 3.
- For the sequence "BETA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, GAMMA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, BETA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, BETA", the method would return `3`, as the number of occurrences (3) of the pattern is greater than or equal to 3.
- For the sequence "BETA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, GAMMA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, BETA, **ALPHA, GAMMA, BETA, GAMMA, BETA**, BETA, ALPHA, ALPHA, **ALPHA, GAMMA, BETA, GAMMA, BETA**", the method would return `4`, as the number of occurrences (4) of the pattern is greater than or equal to 3.


Your task is to write property-based tests (using _jqwik_) for this `numberOfOccurrences` method.

Some tips:

* To create an arbitrary for a `SignalType`, you can use `Arbitraries.of(SignalType.class)`.
* Depending on the way you write your tests, you might find the following helper method useful: [Collections#indexOfSubList(List,List)](https://weblab.tudelft.nl/docs/java/16/api/java.base/java/util/Collections.html#indexOfSubList(java.util.List,java.util.List)) (right-click and open in new tab for its Javadoc). It finds the index of a sublist in a list.
* Feel free to use the [_jqwik_ manual](https://weblab.tudelft.nl/docs/jqwik/1.5.1/docs/1.5.1/user-guide.html) (right-click and open in a new tab)
