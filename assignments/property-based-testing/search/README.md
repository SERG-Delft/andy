In this exercise, you will be testing the `search` method.

> This method returns the index of the given value in the array starting at the given index.

Use **property-based testing** to create a suitable test suite for this method.

Tips:

- Use the [JQWik manual](https://jqwik.net/docs/current/user-guide.html)
- You can generate a list of random integers using something like:
`@ForAll @Size(value=50) List<@IntRange(min = -1000, max = 1000) Integer> numbers`.  
- If you want to convert a `List<Integer>` to an `int[]`, which is what the method under test receives, you may use the provided `convertListToArray` utility method.  
- Only use property-based testing to test clear properties of the program. Feel free to use traditional test methods to test behavior that does not require property-based testing. There is at least one clear property to be tested in this exercise. 