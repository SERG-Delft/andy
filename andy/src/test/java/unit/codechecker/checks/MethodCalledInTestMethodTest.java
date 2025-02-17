package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.MethodCalledInTestMethod;
import org.junit.jupiter.api.Test;
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

    @Test
    void shouldNotBreakWhenAnonymousClassesAreUsed() { // was breaking in midterm 2021
        Check check = new MethodCalledInTestMethod("getPoints");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void shouldCheckForMethodCallsInsideAnonymousClasses() {
        Check check = new MethodCalledInTestMethod("disableGravity");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void shouldIgnoreAnnotationsInsideAnonymousClasses() {
        Check check = new MethodCalledInTestMethod("fromString");
        run("StackOverflowTestWithAnonymousClass.java", check);
        assertThat(check.result()).isFalse();
    }

    @Test
    void shouldMatchExpression() { // checks whether the alternate constructor works
        Check check = new MethodCalledInTestMethod("Utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isTrue();

        check = new MethodCalledInTestMethod("MethodCalled.Utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isTrue();

        check = new MethodCalledInTestMethod("utils", "fromString");
        run("MethodCalled.java", check);
        assertThat(check.result()).isFalse();
    }

}
