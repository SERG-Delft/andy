package nl.tudelft.cse1110.codechecker.engine;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrCheckTest {

    private SingleCheck check1;
    private SingleCheck check2;

    private OrCheck orCheck;

    @BeforeEach
    void setup() {
        check1 = mock(SingleCheck.class);
        check2 = mock(SingleCheck.class);

        when(check1.getFinalResult()).thenReturn(true);
        when(check2.getFinalResult()).thenReturn(false);

        orCheck = new OrCheck(1, "orDescription", List.of(check1, check2));
    }

    @Test
    void getFinalResultIsTrueIfAnyCheckIsTrue() {
        orCheck.runCheck(null);
        assertThat(orCheck.getFinalResult()).isTrue();
    }

    @Test
    void getFinalResultReturnsFalse() {
        orCheck.runCheck(null);
        when(check1.getFinalResult()).thenReturn(false);

        assertThat(orCheck.getFinalResult()).isFalse();
    }

    @Test
    void runCheckRunsAllChecks() {
        CompilationUnit unit = mock(CompilationUnit.class);

        orCheck.runCheck(unit);

        verify(check1).runCheck(unit);
        verify(check2).runCheck(unit);
    }


    @Test
    void testSameObjectEquals() {

        boolean result = orCheck.equals(orCheck);

        assertThat(result).isTrue();
    }


    @Test
    void testNotEqualsNull() {

        boolean result = orCheck.equals(null);

        assertThat(result).isFalse();
    }

    @Test
    void testNotEqualsAnotherClass() {

        boolean result = orCheck.equals(AndCheck.class);

        assertThat(result).isFalse();
    }

}
