package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.NumberOfExampleBasedTestMethods;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberOfExampleBasedTestMethodsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "1, false",
            "2, false",
            "3, true"
    })
    void classWithTests(int maximumNumberOfTests, boolean expectation) {
        Check check = new NumberOfExampleBasedTestMethods(Comparison.LTE, maximumNumberOfTests);
        run("MixOfJqwikAndExampleBasedTests.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @Test
    void classWithoutExampleBasedTests() {
        Check check = new NumberOfExampleBasedTestMethods(Comparison.EQ, 0);
        run("ManyJQWikTests.java", check);
        assertThat(check.result()).isTrue();
    }
}
