<!--NO_HARDWRAPS-->

In this exercise, you will be testing the `unique` method.

> For a given array of doubles, this method returns a new array, 
without all the duplicated values and sorted in descending order.

Use **property-based testing** to create a suitable test suite for this method.

Tips:

* No need for tests that try out nulls.
* AssertJ has a nice assertion to ensure the order of the elements. `assertThat(...).isSortedAccordingTo(...)`. 
Read the manual to understand how it works.
* You may need to convert a list of integers (which JQWik generates easily) to an array of doubles (which is the input of the method). We give you the `convertListToArray` utility method for that.
* Have the [JQWik manual](https://jqwik.net/docs/current/user-guide.html) at hand.
