In this question you are asked to write Selenium tests for a website called *Today's Events*. This is a very simple website that allows you to keep track of events that occur today. You can add an event by specifying its start and end time as well as a short description. It is not allowed to have overlapping events.

You can find the website here:

 https://weblab.tudelft.nl/docs/cse1110/639bcbc2dfac2ed854e7950561373ed4/
(right click and open in a new tab)

_To view the HTML source of the page, you can right-click on the page after opening it and choose 'Inspect' or 'View Page Source'._

_Note: Do not use `driver.get()` to navigate to this URL; `CSE1110.createDriver()` already does that for you._

You should test (only) the following functionality:

- **Adding events** - works correctly
- **Attempting to add an event where the start time falls between the start and end times of an existing event.** - an error is shown. (Do not worry about boundary analysis here. The point is to check whether an error is shown.)

Please use the provided driver as created using the `CSE1110.createDriver()` method, which creates a WebDriver usable by WebLab and navigates to the home page of the website under test. Do not use `driver.get()` to navigate to the webpage! `CSE1110.createDriver()` creates a driver that is already on the right page.
