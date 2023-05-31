In this question you are asked to write Selenium tests for a website called *Mauricio Airways*, which you can find here:

https://weblab.tudelft.nl/docs/cse1110/24083e10369bdf336450fa9b5fe3052c/index.html
(right click and open in a new tab)

_To view the HTML source of the page, you can right-click on the page after opening it and choose 'Inspect' or 'View Page Source'._

_Note: Do not use `driver.get()` to navigate to this URL; `CSE1110.createDriver()` already does that for you._

You should test the following functionality:

- Selecting a flight and booking a valid number of seats - works correctly
- Selecting a flight and attempting to book more seats than there are seats remaining for that flight - an error is shown

<!-- You should write tests for booking tickets. This can be done on the website by selecting a flight, specifying the number of tickets to buy and clicking `Book`. You should test the booking functionality; you do **not** have to test toggling dark/light mode. -->

Please use the provided driver as created using the `CSE1110.createDriver()` method, which creates a WebDriver usable by WebLab and navigates to the home page of the website under test. Do not use `driver.get()` to navigate to the webpage! `CSE1110.createDriver()` creates a driver that is already on the right page.
