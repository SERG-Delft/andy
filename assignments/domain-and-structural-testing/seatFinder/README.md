For this exercise you will test the `getCheapestPrice` method, which is used to ensure that people can get the cheapest tickets possible for a train.
For this train, each seat has a price and can be reserved.

The seats are represented by two arrays, one for the prices and one for whether a seat is already taken.
For both arrays, the `i`th position corresponds to seat `i`.
As an example, in a train with two seats we could have arrays `[5.0, 10.0]` and `[true, false]`, where the seat of 5 euros is already reserved.

A short description of the method:

> The method receives two arrays representing the seats, as well as an integer which indicates the number of requested seats. It calculates the cheapest total price for these seats. If the total price is greater than 100, we apply a discount.

You can find the code of this method and the Javadoc with more details in the Library tab.

Use **specification-based testing** to create a strong test suite for this method. Then, augment your test suite with **structural testing**.
