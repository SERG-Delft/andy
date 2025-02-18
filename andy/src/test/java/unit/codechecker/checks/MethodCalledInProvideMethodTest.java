package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.MethodCalledInProvideMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodCalledInProvideMethodTest extends ChecksBaseTest {
    @Test
    void shouldMatchMethodCallInProperty() {
        Check check = new MethodCalledInProvideMethod("valueOf");
        run("MethodCalledInProvideMethod.java", check);
        assertThat(check.result()).isTrue();

        check = new MethodCalledInProvideMethod("Integer", "valueOf");
        run("MethodCalledInProvideMethod.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void shouldNotMatchMethodCallElsewhere() {
        Check check = new MethodCalledInProvideMethod( "sum");
        run("MethodCalledInProvideMethod.java", check);
        assertThat(check.result()).isFalse();
    }
}
