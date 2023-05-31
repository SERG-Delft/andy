# Property-based testing
In this folder, you find lots of exercises for you to practice property-based testing.

- Take a look at the documentation of the problem. You have some information in the description of the question. Some others also have a JavaDoc near the method.
- Try and establish properties from the description and implementation of the method.
- Test these properties using [`jqwik`](https://jqwik.net).

Try to automate your property tests to ensure your properties are well-tested. Besides that, when generating random inputs do NOT add too many constraints but use a generator to ensure enough test cases are generated in a sufficient amount of time.
