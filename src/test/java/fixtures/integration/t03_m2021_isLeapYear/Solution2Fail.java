package fixtures.integration.t03_m2021_isLeapYear;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Solution2Fail {
    private final LeapYear leapYear = new LeapYear();

    @Property
    void leapYears(@ForAll("leapYears") int year) {
        assertTrue(LeapYear.isLeapYear(year));
    }

    @Property
    void notLeapYears(@ForAll("notLeapYears") int year) {
        assertFalse(LeapYear.isLeapYear(year));
    }

    @Provide
    Arbitrary<Integer> leapYears() {
        return Arbitraries.of(1932,1984,2036,2020,2016,1852,400,4);
    }

    @Provide
    Arbitrary<Integer> notLeapYears() {
        return Arbitraries.of(1700,2021,1800,2022, 39,100);
    }
}
