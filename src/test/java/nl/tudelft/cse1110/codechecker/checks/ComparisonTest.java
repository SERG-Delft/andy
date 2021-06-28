package nl.tudelft.cse1110.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ComparisonTest {

    @ParameterizedTest
    @CsvSource({
            "LT,1,2,true","LT,2,1,false",
            "LTE,1,2,true","LTE,2,1,false", "LTE,2,2,true",
            "GT,1,2,false","GT,2,1,true",
            "GTE,1,2,false","GTE,2,1,true", "LTE,2,2,true",
            "EQ,1,2,false","EQ,1,1,true"
    })
    void comparison(String operator, int actual, int expected, boolean result) {
        assertThat(ComparisonFactory.build(operator).compare(actual, expected))
                .isEqualTo(result);
    }
}
