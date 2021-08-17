package nl.tudelft.cse1110.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodCalledInTestMethodTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "update, true",
            "save, true",
            "persist, true",
            "retrieve, false"
    })
    void methodInvocationsInTestMethod(String methodName, boolean expectation) {
        Check check = new MethodCalledInTestMethod(methodName);
        run("MethodCalled.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
