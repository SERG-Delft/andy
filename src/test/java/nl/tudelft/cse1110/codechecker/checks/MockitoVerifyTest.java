package nl.tudelft.cse1110.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoVerifyTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"add,1,true", "add,2,false", "remove,1,false", "remove,2,true", "contains,3,true", "clear,1,true"})
    void findCallsToVerify(String methodWeExpectAVerify, String numberOfOccurrences, boolean expectation) {
        Check check = new MockitoVerify(Arrays.asList(methodWeExpectAVerify, "TEST", "EQ", numberOfOccurrences));
        run("MockitoVerifyCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({"add,1,true", "add,2,false"})
    void findCallsToVerifyInAfterEach(String methodWeExpectAVerify, String numberOfOccurrences, boolean expectation) {
        Check check = new MockitoVerify(Arrays.asList(methodWeExpectAVerify, "AFTEREACH", "EQ", numberOfOccurrences));
        run("MockitoVerifyCalls.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({"remove,1", "contains,2", "get, 1"})
    void checkTheUseOfNever(String methodWeExpectAVerify, String numberOfOccurrences) {
        Check check = new MockitoVerify(Arrays.asList(methodWeExpectAVerify, "TEST", "EQ", numberOfOccurrences, "true"));
        run("MockitoVerifyNeverCalls.java", check);
        assertThat(check.result()).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"isEmpty", "clear"})
    void times0IsTheSameAsNever(String methodWeExpectAVerify) {
        Check check = new MockitoVerify(Arrays.asList(methodWeExpectAVerify, "TEST", "EQ", "1", "true"));
        run("MockitoVerifyNeverCalls.java", check);
        assertThat(check.result()).isTrue();
    }

}
