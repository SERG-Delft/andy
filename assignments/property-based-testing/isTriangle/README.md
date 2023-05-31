In this exercise, you will be testing the `isTriangle` method.

> This method returns whether a triangle is valid or not. Triangles are valid when no side is bigger than the sum of the other two sides. The implementation is quite straightforward.

Use **property-based testing** to create a suitable test suite for this method.

Tips:

- You can skip tests with negative numbers.
- Your solution should kill 10 mutants (one of them can't be killed)
- You will have to generate sets of `(a,b,c)`, the sizes of the three sides of the triangle. We give you an inner class `ABC`, which is just a class that holds the three sides. You should make JQWik provide different `ABC`s.
- Have the [JQWik manual](https://jqwik.net/docs/current/user-guide.html) at hand.