package delft;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ZigZagTest {


    @ParameterizedTest
    @CsvSource({
            "A,A",
            "AB,AB"
    })
    void singleRow(String s, String expected) {
        assertThat(new ZigZag().zigzag(s,1))
                .isEqualTo(expected);
    }

    @Test
    void multipleRows() {
        assertThat(new ZigZag().zigzag("PAYPALISHIRING",2))
                .isEqualTo(
                        "PYAIHRN\n" +
                        "APLSIIG"
                );

        assertThat(new ZigZag().zigzag("PAYPALISHIRING",3))
                .isEqualTo(
                        "P A H N\n" +
                        "APLSIIG\n" +
                        "Y I R"
                );

        assertThat(new ZigZag().zigzag("PAYPALISHIRING",4))
                .isEqualTo(
                        "P  I  N\n" +
                        "A LS IG\n" +
                        "YA HR\n" +
                        "P  I"
                );

        assertThat(new ZigZag().zigzag("PAYPALISHIRING",5))
                .isEqualTo(
                        "P   H\n" +
                        "A  SI\n" +
                        "Y I R\n" +
                        "PL  IG\n" +
                        "A   N"
                );
    }

    @Test
    void moreRowsThanChars() {
        assertThat(new ZigZag().zigzag("ABC",4))
                .isEqualTo("A\nB\nC");
    }

    @Test
    void invalidStrings() {
        assertThatThrownBy(() -> new ZigZag().zigzag("", 1))
                .isInstanceOf(IllegalArgumentException.class);

        StringBuilder longString = new StringBuilder();

        // <= 1000
        for(int i = 0; i < 1000; i++) {
            longString.append("a");
        }
        assertThat(new ZigZag().zigzag(longString.toString(), 1)).isEqualTo(longString.toString());

        // > 1000
        longString.append("a");
        assertThatThrownBy(() -> new ZigZag().zigzag(longString.toString(), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void invalidNumberOfRows() {
        assertThatThrownBy(() -> new ZigZag().zigzag("somestring", 0))
                .isInstanceOf(IllegalArgumentException.class);

        // <= 1000
        assertThat(new ZigZag().zigzag("somestring", 1000)).isEqualTo("s\no\nm\ne\ns\nt\nr\ni\nn\ng");
        // > 1000
        assertThatThrownBy(() -> new ZigZag().zigzag("somestring", 1001))
                .isInstanceOf(IllegalArgumentException.class);

    }


}
