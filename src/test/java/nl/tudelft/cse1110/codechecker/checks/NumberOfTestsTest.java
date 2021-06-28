package nl.tudelft.cse1110.codechecker.checks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberOfTestsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "1, true",
            "2, true",
            "3, true",
            "4, false",
    })
    void classWithTests(String minimumNumberOfTests, boolean expectation) {
        Check check = new NumberOfTests(Arrays.asList("GTE", minimumNumberOfTests));
        run("ManyTests.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @Test
    void classWithoutTests() {
        Check check = new NumberOfTests(Arrays.asList("GTE", "1"));
        run("NoTests.java", check);
        assertThat(check.result()).isFalse();
    }


}
