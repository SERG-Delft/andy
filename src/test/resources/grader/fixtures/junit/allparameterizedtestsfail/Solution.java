package tudelft.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Student accidentally passed the first argument (int result) as 3rd argument, making all tests fail.
class TwoIntegersTest {

    private final TwoIntegers adder = new TwoIntegers();

    //valid input cases
    @ParameterizedTest(name = "num1={0}, num2={1}, result={2}")
    @CsvSource({
            "50, 50, 100",
            "1, 50, 51",
            "99, 50, 149",
            "50, 50, 100",
            "50, 1, 51",
            "50, 99, 149"
    })
    void sumValidCases(int result, int num1, int num2) {
        Assertions.assertEquals(result, adder.sum(num1, num2));
    }
}