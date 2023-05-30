package delft;

import java.util.Comparator;
import java.util.stream.IntStream;

class SeatFinder {

    private SeatFinder() {
        // Empty constructor
    }

    /**
     * Finds the cheapest seats and returns the total price for those seats.
     *
     * If there are fewer seats available than requested,
     * only the prices for the ones that are available are counted.
     *
     * When the total price of the seats is larger than 100,
     * a discount of 5 euros is applied.
     *
     * The prices and taken arrays cannot be null and need to have the same size.
     * The number of seats requested should be at least 1.
     *
     * @param prices an array indicating the price of each seat
     * @param taken an array indicating whether a seat has been taken already
     * @param numberOfSeats the number of seats requested
     * @throws IllegalArgumentException when any of the arguments do not
     *                                  adhere to their requirements
     * @return total price for the seats
     */
    public static double getCheapestPrice(double[] prices, boolean[] taken, int numberOfSeats) {
        if (prices == null || taken == null) {
            throw new IllegalArgumentException("prices and taken arrays cannot be null");
        }
        if (prices.length != taken.length) {
            throw new IllegalArgumentException("prices and taken arrays do not have the same length");
        }
        if (numberOfSeats <= 0) {
            throw new IllegalArgumentException("The number of seats has to be at least 1");
        }

        int[] seats = IntStream.range(0, prices.length)
                .boxed()
                .sorted(Comparator.comparingDouble(s -> prices[s]))
                .mapToInt(Integer::intValue)
                .toArray();

        int numberOfTickets = 0;
        double totalPrice = 0;
        for (int i = 0; i < seats.length; i++) {
            int seat = seats[i];
            if (!taken[seat]) {
                totalPrice += prices[seat];
                numberOfTickets++;
            }
            if (numberOfTickets == numberOfSeats) {
                break;
            }
        }

        if (totalPrice > 100.00) {
            return totalPrice - 5.00;
        }
        return totalPrice;
    }
}
