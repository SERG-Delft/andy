package tudelft.domain;

import org.junit.jupiter.api.Test;
import tudelft.domain.LeapYear;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeapYearTest {

    private final LeapYear leapYear = new LeapYear();


    @Test
    public void leapYearsThatAreNonCenturialYears() {
        assertTrue(leapYear.isLeapYear(2016));
    }

    // assertFalse should be assertTrue
    @Test
    public void leapCenturialYears() {
        assertFalse(leapYear.isLeapYear(2000));
    }

    @Test
    public void nonLeapCenturialYears() {
        assertFalse(leapYear.isLeapYear(1500));
    }

    @Test
    public void nonLeapYears() {
        assertFalse(leapYear.isLeapYear(2017));
    }
}
