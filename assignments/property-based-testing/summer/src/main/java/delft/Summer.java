package delft;

import java.util.List;

class Summer {

    private Summer() {
        // Empty constructor
    }

    /**
     * This method predicts whether it is summer.
     * If at least 75% of the temperature values provided are 20 degrees or above, it is summer.
     * Otherwise, it is not summer.
     *
     * @param temperatures The list of temperature values
     * @return the probability of it being summer
     */
    public static boolean isItSummer(List<Double> temperatures) {
        int count20OrAbove = 0;

        for (Double temp : temperatures) {
            if (temp >= 20) {
                count20OrAbove++;
            }
        }

        return count20OrAbove >= temperatures.size() * 0.75f;
    }
}
