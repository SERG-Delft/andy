<!--NO_HARDWRAPS-->

A local bookstore has asked for your help in testing their new online store. The `getPriceForCart` receives a map containing a list of ISBNs and their respective quantity of books that should be bought. The program then returns the final price of the order. If there are not enough books in stock, the program puts all the books it can in the order, and returns a list of what's missing.

Your goal is to perform domain testing and structural testing on the `getPriceForCart` method. **You should make use of mocks.**

* Note that Pitest identifies 9 mutants in the `BookStore` class, but 1 is unfeasible. Therefore, killing the 8 remaining mutants is enough for 100% mutation coverage.
* AssertJ has many methods to help you in asserting the content of a map, e.g., `assertThat(...).containsEntry(key, value)` or `assertThat(...).containsExactly(entry(key, value))`.
* Mock only classes that deserve to be mocked.
