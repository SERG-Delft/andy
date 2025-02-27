package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class PalindromeTests {
    @Property
    void metaAlwaysReturnsFalse(@ForAll @AlphaChars @StringLength(min = 1, max = 100) String word) {
        boolean result = Palindrome.isPalindrome(word);
        if (word.equalsIgnoreCase(reverse(word))) {
            Assertions.assertTrue(result);
        } else {
            Assertions.assertFalse(result);
        }
    }

    // Utility method to reverse a string
    static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }
}
