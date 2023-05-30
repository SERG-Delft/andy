package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class DelftStringUtilitiesTest {
    
    @ParameterizedTest
    @MethodSource("generator")
    void test(String text, String searchString, String replacement, int max, boolean ignoreCase, String expectedResult) {
        assertThat(DelftStringUtilities.replace(text, searchString, replacement, max, ignoreCase)).isEqualTo(expectedResult);
    }
    
    private static Stream<Arguments> generator() {
        return Stream.of(
                // Test where the text is null
                Arguments.of(null, "day", "night", -1, false, null),

                // Test where the search text is null
                Arguments.of("I went to the beach the other day.", null, "mall", 4, false, "I went to the beach the other day."),

                // Test where the replacement text is null
                Arguments.of("I went to the beach the other day.", "beach", null, 2, false, "I went to the beach the other day."),
                
                // Test where the text is empty
                Arguments.of("", "day", "night", 1, false, ""),
                
                // Test where the search string is empty
                Arguments.of("I like programming.", "", "hacking", 1, false, "I like programming."),

                // Test where the maximum number of replacements is 0
                Arguments.of("I like programming.", "programming", "hacking", 0, false, "I like programming."),

                // Test where the search string is empty
                Arguments.of("I like programming.", "", "hacking", 1, false, "I like programming."),

                // Test where the ignore case is true
                Arguments.of("SQT, ADS, OOP", "sqt", "PTS", 1, true, "PTS, ADS, OOP"),
                
                // Test where the ignore case is false
                Arguments.of("IDM, LA, RNL", "idm", "PTS", 1, false, "IDM, LA, RNL"),

                // Test where the search string is not contained in the text
                Arguments.of("I went to the beach the other day.", "night", "afternoon", 1, true, "I went to the beach the other day."),

                // Test where the search string is not contained in the text
                Arguments.of("I went to the beach the other day.", "night", "afternoon", 1, true, "I went to the beach the other day."),

                // Test where the replacement string is longer than the search string
                Arguments.of("SQT, ADS, OOP", "SQT", "OOPP", 1, false, "OOPP, ADS, OOP"),

                // Test where the replacement string is shorter than the search string
                Arguments.of("SQT, ADS, OOP", "SQT", "LA", 2, false, "LA, ADS, OOP"),

                // Test where the maximum is negative
                Arguments.of("OOPP, LA, IDM, Statistics", "Statistics", "SQT", -1, false, "OOPP, LA, IDM, SQT"),

                // Test where the maximum is 0
                Arguments.of("OOPP, LA, IDM, Calculus", "Calculus", "SQT", 0, false, "OOPP, LA, IDM, Calculus"),

                // Test where the maximum number is 64
                Arguments.of(("abc").repeat(64), "abc", "def", 64, false, ("def").repeat(64)),

                // Test where the maximum number is higher than 64
                Arguments.of(("abc").repeat(70), "abc", "def", 70, false, ("def").repeat(70)),

                // The replacement string is longer than the search string and the maximum number is more than 64
                Arguments.of(("abc").repeat(64), "abc", "defg", 64, false, ("defg").repeat(64)),

                // The maximum number is 16
                Arguments.of(("abc").repeat(16), "a", "aaa", 16, true, ("aaabc").repeat(16)),

                // The replacement string is shorter than the long string
                Arguments.of("SQT, IDM, LA", "SQ", "F", 1, false, "FT, IDM, LA"),

                // Test where the search string is both lowercase and uppercase letters, case insensitivity
                Arguments.of("SqT, SQT, SQt", "qt", "AB", 2, true, "SAB, SAB, SQt")
        );
    }

}