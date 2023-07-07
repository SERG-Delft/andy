package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.MethodCalledAnywhere;
import nl.tudelft.cse1110.andy.codechecker.checks.MethodCalledInTestMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodCalledAnywhereTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
        "update, true",
        "save, true",
        "persist, true",
        "retrieve, true"
    })
    void methodInvocationsInTestMethod(String methodName, boolean expectation) {
        Check check = new MethodCalledAnywhere(methodName);
        run("MethodCalled.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @Test
    void shouldIgnoreAnonymousClasses() { // was breaking in midterm 2021
        Check check = new MethodCalledInTestMethod("getPoints");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

}
