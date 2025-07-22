package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.ConditionalExpressionUsed;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalExpressionUsedTest extends ChecksBaseTest {
    @Test
    void conditionalExpressionUsed() {
        Check check = new ConditionalExpressionUsed();
        run("ConditionalExpressionUsed.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void conditionalExpressionNotUsed() {
        Check check = new ConditionalExpressionUsed();
        run("NoTests.java", check);
        assertThat(check.result()).isFalse();
    }
}
