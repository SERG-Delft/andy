package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.IfStatementUsedAnywhere;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IfStatementUsedAnywhereTest extends ChecksBaseTest {
    @Test
    void ifStatementUsed() {
        Check check = new IfStatementUsedAnywhere();
        run("IfStatementUsed.java", check);
        assertThat(check.result()).isTrue();
    }

    @Test
    void ifStatementNotUsed() {
        Check check = new IfStatementUsedAnywhere();
        run("NoTests.java", check);
        assertThat(check.result()).isFalse();
    }
}
