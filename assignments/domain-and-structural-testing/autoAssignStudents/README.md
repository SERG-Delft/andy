The following piece of code is part of a system that keeps track of students and which workshops they take. The auto-assign functionality assigns students to all the workshops in a list. It assigns students to the earliest possible date of that workshop. If the workshop run in that date has no more spots left for student, the algorithm looks for the next available date. If there's no available date at all, the program logs it.

This is a complex business rule, and so, the code is split into a few classes. However, your task is to test the entire use case. Your tests should always start from calling the `AutoAssigner#assign` method. Write a strong test suite that achieves 100% branch and mutation coverage and exercises the boundaries of the program.

Some tips:

* You may assume that input parameters are never null.

* Make sure there is always at least one entry in the `Map`. (The date may have 0 spots left from the beginning, but still, there's at least one date in the map.) You do not have to write tests for the case where the `Map` is empty.

* We will only consider coverage of the `AutoAssigner` and `Workshop` classes. The other classes are important, of course, but as you see in the code, the main logic of the algorithm lies in these two classes only.

* AssertJ offers the `containsExactlyInAnyOrder` assertion that is useful to assert that lists contain a precise set of strings, but in any order. 

* We give you the `date()` helper method that instantiates a `ZonedDateTime` based on year, month, day, hour, and minute.
