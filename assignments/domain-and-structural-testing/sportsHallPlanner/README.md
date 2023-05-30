In this exercise, you should test the `planHalls` method. It is used to create a sports hall planning given a list of requests. For example, one might request a "sports hall *near the city centre* that has at least *1 volleyball field*". The method will then find a suitable hall and assign the request to that hall (and the same happens for any other requests that might be in the list).

The method receives both a list of sports halls and a list of requests. See its description below:

> This method assigns the given requests to the available sports halls in the list. A request can only be assigned to a sports hall that has enough sports fields of the given type and all the required properties.<br> Each sports hall can only be used to fulfill (at most) one request. Not all sports halls need to be used, but all requests have to be assigned to a sports hall. Whenever a request can be fulfilled by multiple halls, the first hall in the list should be chosen. If there are multiple requests that can be assigned to multiple halls, the first request will be assigned to the first hall, the second to the second and so on. Finally, there should not be any duplicate halls in the input list.

You can find the code of this method and the Javadoc with more details in the Library tab. The classes that are used by the method can also be found there.

Note that, in real life, to properly test this feature, you would also need to test the logic in the `Request` constructor and the `canFulfillRequest` method. **However, for this exam, we will only look at the tests for the `planHalls` method.**

Use **specification-based testing** to create a strong test suite for this method. Then, augment your test suite with **structural testing**.
