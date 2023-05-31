In this exercise, you will be testing the `makeReservation` method from SoftWHERE, a fake travel agency created and run by developers.

> This method makes a reservation for a specific trip for a list of people.

Use **test doubles** to create a suitable test suite for this method.

Tips:

- There are many ways to instantiate a `LocalDate`. For example, you can use the static call `LocalDate.of(1998,2,5)`. In this example, the date points to February 5th, 1998.
- Mock only classes that deserve to be mocked.