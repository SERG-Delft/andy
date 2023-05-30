This is an assignment for testing Frank's Bookstore, which is a simple web application. You can download the application below.

Explore how the application works and then write **Selenium** tests for *adding a book*.

You may assume that **only** the following authors are already in the system:

 - ID 1: Mauricio Aniche
 - ID 2: Dan Brown
 - ID 3: J.R.R. Tolkien

To retrieve the WebDriver which you should use for your connections, use `CSE1110.getDriver()`. Do not instantiate a driver yourself!

Make sure you don't remove the call to `driver.close()` in `@AfterEach`, otherwise you will get timeout errors!
