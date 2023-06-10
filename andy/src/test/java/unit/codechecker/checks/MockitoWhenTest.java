package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.MockitoWhen;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoWhenTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({
            "add,1,false", "add,2,true", "add,3,false",
            "remove,1,false","remove,2,true","remove,3,false",
            "contains,6,true","contains,7,false","contains,8,false",
            "equals,0,false","equals,4,true","equals,5,false",
            "toString,1,false","toString,3,true","toString,4,false"
    })
    void findCallsToWhen(String methodThatWeExpectAMockitoWhen, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoWhen(methodThatWeExpectAMockitoWhen, Comparison.EQ, numberOfOccurrences);
        run("MockitoWhenCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
