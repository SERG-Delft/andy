package nl.tudelft.cse1110.andy.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TestMethodsHaveAssertionsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"AllTestsWithAssertions,true", "TestMissingAssertion,false", "TestUsingAssertThrows,true"})
    void lookForAssertions(String classToRun, boolean expectation) {
        Check check = new TestMethodsHaveAssertions();
        run(classToRun + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
