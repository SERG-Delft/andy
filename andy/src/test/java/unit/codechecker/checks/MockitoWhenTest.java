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
            "add,0,false", "add,1,true", "add,2,false",
            "remove,1,false","remove,2,true","remove,3,false",
            "toString,0,false","toString,1,true","toString,2,false",
            "get,1,false", "get,2,true", "get,3,false",
            "contains,2,false", "contains,3,true", "contains,4,false"
    })
    void findCallsToWhen(String methodThatWeExpectAMockitoWhen, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoWhen(methodThatWeExpectAMockitoWhen, Comparison.EQ, numberOfOccurrences);
        run("MockitoWhenCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
