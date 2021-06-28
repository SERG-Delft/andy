package fixtures.integration.t03_m2021_isLeapYear;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Solution1Pass {


    // This test whether a common regular year (not divisible by 4) returns false.
    @Property
    void testCommonNotLeapYear(@ForAll("notDivisibleBy4") int year) {
        assertFalse(LeapYear.isLeapYear(year));
    }

    // This test whether a common leap year (divisible by 4, not by 100) returns true.
    @Property
    void testCommonLeapYear(@ForAll("divisibleBy4NotBy100") int year) {
        assertTrue(LeapYear.isLeapYear(year));
    }

    // This test whether a rare regular year (divisible by 100, not by 400) returns false.
    @Property
    void testRareNotLeapYear(@ForAll("divisibleBy100NotBy400") int year) {
        assertFalse(LeapYear.isLeapYear(year));
    }

    // This test whether a rare leap year (divisible by 400) returns true.
    @Property
    void testRareLeapYear(@ForAll("divisibleBy400") int year) {
        assertTrue(LeapYear.isLeapYear(year));
    }

    @Provide
    Arbitrary<Integer> divisibleBy400() {
        return Arbitraries.integers()
                .filter((x) -> x % 400 == 0);
    }

    @Provide
    Arbitrary<Integer> divisibleBy100NotBy400() {
        return Arbitraries.integers()
                .filter((x) -> x % 100 == 0)
                .filter((x) -> x % 400 != 0);
    }

    @Provide
    Arbitrary<Integer> divisibleBy4NotBy100() {
        return Arbitraries.integers()
                .filter((x) -> x % 4 == 0)
                .filter((x) -> x % 100 != 0);
    }

    @Provide
    Arbitrary<Integer> notDivisibleBy4() {
        return Arbitraries.integers()
                .filter((x) -> x % 4 != 0);
    }

}
