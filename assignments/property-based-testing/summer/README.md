_You may ignore the following paragraph if you wish, as it is not relevant for the solution._

_Amy the Hippo wants to know whether summer is finally here. However, she does not have a calendar, so she measures the temperature in different places around the swamp, and then uses a program to tell her whether it is summer, based on those values._

The `isItSummer` method (in the library tab) takes a list of temperature values, and returns true if and only if _at least 75%_ of the temperature values are `20` degrees or above.

Your task is to write **property-based tests** (using _jqwik_) for this `isItSummer` method.

Some tips:

* A good property-based test for this problem ensures 100% branch + condition coverage.
* Have the [_jqwik_ manual](https://weblab.tudelft.nl/docs/jqwik/1.5.1/docs/1.5.1/user-guide.html) at hand (right-click and open in a new tab)
