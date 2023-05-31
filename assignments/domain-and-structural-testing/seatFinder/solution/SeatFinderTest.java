package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.assertj.core.data.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;


class SeatFinderTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("validCaseGenerator")
    void validCasesTests(String description, double expectedResult,
                         double[] prices, boolean[] taken, int numberOfSeats) {
        assertThat(SeatFinder.getCheapestPrice(prices, taken, numberOfSeats))
                .isCloseTo(expectedResult, Percentage.withPercentage(0.1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("illegalArgumentsGenerator")
    void illegalArgumentsTests(String description, double[] prices, boolean[] taken, int numberOfSeats) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> SeatFinder.getCheapestPrice(prices, taken, numberOfSeats));
    }

    private static Stream<Arguments> validCaseGenerator() {
        return Stream.of(
                Arguments.of("Single seat, gets taken",
                        13.45, arrayOf(13.45), arrayOf(false), 1),

                Arguments.of("Single seat, already taken (and discount condition out point)",
                        0, arrayOf(23.67), arrayOf(true), 1),

                Arguments.of("Multiple seats, same prices, none taken, all requested",
                        33.33, arrayOf(11.11, 11.11, 11.11), arrayOf(false, false, false), 3),

                Arguments.of("Multiple seats, same prices, all are already taken",
                        0, arrayOf(23.56, 23.56), arrayOf(true, true), 2),

                Arguments.of("Multiple seats, same prices, some are already taken, more requested",
                        21.00, arrayOf(10.50, 10.50, 10.50, 10.50), arrayOf(true, false, false, true), 3),

                Arguments.of("Multiple seats, same prices, some are already taken, not all are requested",
                        15.20, arrayOf(7.60, 7.60, 7.60, 7.60), arrayOf(false, false, true, false), 2),

                Arguments.of("Multiple seats, different prices, none taken, one seat requested",
                        6.23, arrayOf(16.78, 32.60, 6.23, 24.53), arrayOf(false, false, false, false), 1),

                Arguments.of("Multiple seats, different prices, none taken, multiple requested",
                        57.84, arrayOf(23.54, 54.21, 34.30), arrayOf(false, false, false), 2),

                Arguments.of("Multiple seats, different prices, some taken, multiple requested",
                        35.68, arrayOf(19.36, 23.75, 17.25, 16.32), arrayOf(false, false, true, false), 2),

                Arguments.of("Discount condition on point (100) does not apply discount",
                        100.00, arrayOf(100.00), arrayOf(false), 1),

                Arguments.of("Discount condition off point (100.01) applies discount",
                        95.01, arrayOf(100.01), arrayOf(false), 1),

                Arguments.of("Discount condition in point (245.00) applies discount",
                        240.00, arrayOf(245.00), arrayOf(false), 1)
        );
    }

    private static Stream<Arguments> illegalArgumentsGenerator() {
        return Stream.of(
                Arguments.of("prices is null",
                        null, arrayOf(true), 1),

                Arguments.of("taken is null",
                        arrayOf(1.0), null, 1),

                Arguments.of("prices and taken have different lengths",
                        arrayOf(1.0, 2.0), arrayOf(false, true, false), 1),

                Arguments.of("numberOfSeats validity condition on point (0)",
                        arrayOf(0.5, 1.2), arrayOf(false, true), 0),

                Arguments.of("numberOfSeats validity condition in point",
                        arrayOf(1.6, 4.0, 3.2), arrayOf(true, false, false), -67)
        );
    }

    private static double[] arrayOf(double... values) {
        return values;
    }

    private static boolean[] arrayOf(boolean... values) {
        return values;
    }
}
