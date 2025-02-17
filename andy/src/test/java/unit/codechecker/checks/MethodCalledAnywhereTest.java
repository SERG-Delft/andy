package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.MethodCalledAnywhere;
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
        "retrieve, true",
        "ceil, false",
    })
    void methodInvocationsInTestMethod(String methodName, boolean expectation) {
        Check check = new MethodCalledAnywhere(methodName);
        run("MethodCalled.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

    @Test
    void shouldNotBreakWhenAnonymousClassesAreUsed() { // was breaking in midterm 2021
        Check check = new MethodCalledAnywhere("getPoints");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void shouldCheckForMethodCallsInsideAnonymousClasses() {
        Check check = new MethodCalledAnywhere("disableGravity");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void shouldMatchExpression() { // checks whether the alternate constructor works
        Check check = new MethodCalledAnywhere("Utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isTrue();

        check = new MethodCalledAnywhere("MethodCalled.Utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isTrue();

        check = new MethodCalledAnywhere("utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isFalse();

        check = new MethodCalledAnywhere("repo", "retrieve");
        run("MethodCalled.java", check);
        assertThat(check.result()).isTrue();
    }
}
