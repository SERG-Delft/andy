This is an assignment for testing the search functionality of Frank's Bookstore, which you can download below. Write Selenium tests for searching for authors and books.

You may assume that **only** the following books are already in the system:

 - "*Effective Software Testing: A Developer's Guide*" by Mauricio Aniche
 - "*The Da Vinci Code*" by Dan Brown
 - "*The Lord of the Rings*" by J.R.R. Tolkien

To retrieve the WebDriver which you should use for your connections, use `CSE1110.getDriver()`. Do not instantiate a driver yourself!

Make sure you don't remove the call to `driver.close()` in `@AfterEach` otherwise you will get timeout errors!
