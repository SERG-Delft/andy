package tudelft.intro;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;

class RomanNumeral {

    private final static Map<Character, Integer> RomanToArabic =
            Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);


    // code refactored
    public int asArabic(String roman) {

        final var iter = new StringCharacterIterator(roman);

        var nextValue = 0;
        var result = 0;
        // processes String backwards
        for (char c = iter.last(); c != CharacterIterator.DONE; c = iter.previous()) {
            var currentValue = RomanToArabic.get(c);
            var isSubtractive = currentValue < nextValue;
            var factor = isSubtractive ? -1 : 1;
            result += factor * currentValue;

            nextValue = currentValue;       // "nextValue" is the value right of current, so old current
        }

        return result;
    }


    // initial version used in book
    public int convert(String s) {
        int convertedNumber = 0;

        for (int i = 0; i < s.length(); i++) {
            int currentNumber = RomanToArabic.get(s.charAt(i));
            int next = i + 1 < s.length() ? RomanToArabic.get(s.charAt(i + 1)) : 0;

            if (currentNumber >= next) {
                convertedNumber += currentNumber;
            } else {
                convertedNumber -= currentNumber;
            }
        }

        return convertedNumber;
    }


}
