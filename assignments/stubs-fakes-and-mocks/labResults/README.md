In this exercise, you will be testing the `getLabResult` method from the (fake) MyHealth app, which allows users to retrieve their medical data. As you can imagine, this kind of data is sensitive and users should only have access to the data they are authorised to view.

A short description of the method under test:

> This method retrieves a specific lab result if the user requesting it is authorised to view it. It logs both an unauthorised request as well as if the lab result doesn't exist.

Use **test doubles** to create a suitable test suite for this method.

**Important:** Use the `Mockito.mock(...)` method to create a mock. Do not use the `@Mock` annotation as Andy still can't recognize it.
