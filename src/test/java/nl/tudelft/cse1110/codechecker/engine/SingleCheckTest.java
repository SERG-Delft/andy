package nl.tudelft.cse1110.codechecker.engine;

import nl.tudelft.cse1110.codechecker.checks.Check;
import nl.tudelft.cse1110.codechecker.checks.CheckFactory;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SingleCheckTest {

    private CheckFactory factory;
    private Check check;
    private CompilationUnit unit;

    @BeforeEach
    void setup() {
        factory = mock(CheckFactory.class);
        check = mock(Check.class);
        when(factory.build(anyString(), anyList())).thenReturn(check);
        unit = mock(CompilationUnit.class);

        when(check.result()).thenReturn(true);
    }

    @Test
    void getFinalResultDoesNotFlipTrue() {

        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, "GTE 0", factory);

        singleCheck.runCheck(null);
        assertThat(singleCheck.getFinalResult()).isTrue();
    }

    @Test
    void getFinalResultDoesNotFlipFalse() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, "GTE 0", factory);

        singleCheck.runCheck(null);
        when(check.result()).thenReturn(false);
        assertThat(singleCheck.getFinalResult()).isFalse();
    }

    @Test
    void getFinalResultFlips() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", true, "GTE 0", factory);

        singleCheck.runCheck(null);

        assertThat(singleCheck.getFinalResult()).isFalse();
    }

    @Test
    void getFinalResultFlipsFalse() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", true, "GTE 0", factory);

        singleCheck.runCheck(null);

        when(check.result()).thenReturn(false);
        assertThat(singleCheck.getFinalResult()).isTrue();
    }

    @Test
    void runCheckRunsTheCheck() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, "GTE 0", factory);

        singleCheck.runCheck(unit);

        verify(check).check(unit);
    }

    @Test
    void runWithoutParams() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);

        singleCheck.runCheck(unit);

        verify(factory).build("rule", Collections.emptyList());
        verify(check).check(unit);
    }

    @Test
    void testSameObjectEquals() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);

        boolean result = singleCheck.equals(singleCheck);

        assertThat(result).isTrue();
    }

    @Test
    void testNotEqualsNull() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);

        boolean result = singleCheck.equals(null);

        assertThat(result).isFalse();
    }

    @Test
    void testNotEqualsAnotherClass() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);

        boolean result = singleCheck.equals(AndCheck.class);

        assertThat(result).isFalse();
    }


    @Test
    void testNotEqualsDifferentFlip() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);
        SingleCheck singleCheck2 = new SingleCheck(1, "rule", "d1", true, null, factory);

        boolean result = singleCheck.equals(singleCheck2);

        assertThat(result).isFalse();
    }

    @Test
    void testNotEqualsDifferentRule() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);
        SingleCheck singleCheck2 = new SingleCheck(1, "rule2", "d1", false, null, factory);

        boolean result = singleCheck.equals(singleCheck2);

        assertThat(result).isFalse();
    }

    @Test
    void testNotEqualsDifferentParams() {
        SingleCheck singleCheck = new SingleCheck(1, "rule", "d1", false, null, factory);
        SingleCheck singleCheck2 = new SingleCheck(1, "rule", "d1", false, "GTA0", factory);

        boolean result = singleCheck.equals(singleCheck2);

        assertThat(result).isFalse();
    }
}
