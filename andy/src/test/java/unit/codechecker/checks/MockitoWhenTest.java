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
            "remove,0,false","remove,1,true","remove,2,false",
            "contains,6,true", "contains,7,false", "contains,8,false",
            "equals,1,false","equals,4,true","equals,5,false",
            "poll,0,false","poll,1,true","poll,2,false",
            "offer,0,false","offer,1,true","offer,2,false",
            "toString,1,false","toString,2,true","toString,3,false"
    })
    void findCallsToWhen(String methodThatWeExpectAMockitoWhen, int numberOfOccurrences, boolean expectation) {
        Check check = new MockitoWhen(methodThatWeExpectAMockitoWhen, Comparison.EQ, numberOfOccurrences);
        run("MockitoWhenCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
