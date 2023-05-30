<!--NO_HARDWRAPS-->

The `add` method receives two numbers, `left` and `right`, both represented as a list of digits.
It adds these numbers and returns the result also as a list of digits. 
For more information about the method, read its Javadoc.

You have to:

* Use domain testing techniques to derive tests for this method. 
* Then, augment your test suite with structural testing.

The method under test receives arguments of type `List<Integer>`. To help you in creating lists of digits in your test code, we provide you with the `numbers()` method. We strongly suggest you to use it:

* `numbers()` -> returns an empty List<Integer>
* `numbers(3,1,4)` -> returns a List<Integer> with the given three elements

(Inspired by LeetCode's Add Two Numbers problem: https://leetcode.com/problems/add-two-numbers/)