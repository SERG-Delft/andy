package delft;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ConnectFour1DTests {
    @Property
    public void testDraw(@ForAll @Chars({'A', 'B'}) String input) {
        String board = input.replace("A", "OXXXO")
                .replace("B", "XOOOX");
        assertThat(ConnectFour1D.calculateResult(board)).isEqualTo(GameResult.DRAW);
    }

    @Property
    public void testXWon(@ForAll @NotBlank @Chars({'A', 'B', 'C'}) String input) {
        String board = input.replace("A", "OXXXO")
                .replace("B", "XOOOX")
                .replace("C", "XXXX");

        if (input.contains("C")) {
            assertThat(ConnectFour1D.calculateResult(board)).isEqualTo(GameResult.X_WON);
        }
        else {
            assertThat(ConnectFour1D.calculateResult(board)).isEqualTo(GameResult.DRAW);
        }
    }

    @Property
    public void testOWon(@ForAll @NotBlank @Chars({'A', 'B', 'C'}) String input) {
        String board = input.replace("A", "OXXXO")
                .replace("B", "XOOOX")
                .replace("C", "OOOO");

        if (input.contains("C")) {
            assertThat(ConnectFour1D.calculateResult(board)).isEqualTo(GameResult.O_WON);
        }
        else {
            assertThat(ConnectFour1D.calculateResult(board)).isEqualTo(GameResult.DRAW);
        }
    }

    @Property
    public void testBothWon(@ForAll("boardWhereBothWon") String input) {
        String board = input.replace("A", "OXXXO")
                .replace("B", "XOOOX")
                .replace("C", "OOOO")
                .replace("D", "XXXX");

        assertThatThrownBy(() -> ConnectFour1D.calculateResult(board)).isInstanceOf(IllegalArgumentException.class);
    }

    @Property
    public void testOtherLettersIgnored(@ForAll("allLettersExceptXO") String input) {
        assertThat(ConnectFour1D.calculateResult(input)).isEqualTo(GameResult.DRAW);
    }

    @Provide
    public Arbitrary<String> boardWhereBothWon() {
        return Arbitraries.strings()
                .withChars('A', 'B', 'C', 'D')
                .filter(s -> s.contains("C") && s.contains("D"));
    }

    @Provide
    public Arbitrary<String> allLettersExceptXO() {
        return Arbitraries.strings().excludeChars('X', 'O');
    }
}
