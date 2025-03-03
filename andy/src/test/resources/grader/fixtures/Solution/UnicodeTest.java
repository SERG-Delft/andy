package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.nio.charset.StandardCharsets;

class UnicodeTest {

    String apple = "🍎";
    String アボカド = "🥑";

    @Test
    void unicodeTest() {
        assertThat(apple.getBytes(StandardCharsets.UTF_8)).containsExactly(0xf0, 0x9f, 0x8d, 0x8e);
        assertThat(アボカド.getBytes(StandardCharsets.UTF_8)).containsExactly(0xf0, 0x9f, 0xa5, 0x91);
    }
}
