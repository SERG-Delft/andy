In this assignment, we consider a one-dimensional version of the popular game Connect Four. A player (either `X` or `O`) wins when their letter occurs four times in a row. For example, when the "board" equals `"XXXXOOO"`, player `X` wins.

The `calculateResult` method in the `ConnectFour1D` class is used to determine who won the game. If both `X` and `O` have four in a row, it throws an `IllegalArgumentException`. If neither of them have four in a row, the method returns `GameResult.DRAW`.

Test the `calculateResult` method using property-based tests (with *jqwik*).

Some tips:

* You may assume that the `calculateResult` and `checkWin` methods both work correctly. You may for instance notice that `"XXXX"` is considered valid input, even though in an actual game of Connect Four the players would take turns (so there would be some occurrences of `"O"` as well). The actual implementation is leading, so `GameResult.X_WON` is the expected output here.
* You do not have to test for `null` values.
* Feel free to limit the length of your generated strings to a maximum of 20 characters, to keep the execution times of jqwik reasonable.
