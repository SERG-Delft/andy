package tudelft.structural;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tudelft.structural.CountLetters;

// In test 1, the expected int should be 2, instead of 1.
// In test 2, the expected int should be 1, instead of 2.

class CountLettersTest {

    // 100% block coverage
    @Test
    public void multipleMatchingWords() {
        int words = new CountLetters().count("cats|dogs");
        Assertions.assertEquals(1, words);
    }

    // Tests false path of CFG (dog doesn't end in s)
    @Test
    public void lastWordDoesNotMatch() {
        int words = new CountLetters().count("cats|dog");
        Assertions.assertEquals(2, words);
    }

}