package tudelft.intro;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// student forgot assertions, but tests should still pass.
class RomanNumeralTest {

    final private RomanNumeral roman = new RomanNumeral();


    @Test
    void convertSingleDigit() {
        int result = roman.asArabic("C");
    }

    @Test
    void convertNumberWithDifferentDigits() {
        int result = roman.asArabic("CCXVI");
    }

    @Test
    void convertNumberWithSubtractiveNotation() {
        int result = roman.asArabic("XL");
    }

}
