package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class DelftStringUtilitiesTest {

	@ParameterizedTest
	@MethodSource("generator")
	void repeat(String description, String originalString, int times, String expectedString) {
		assertThat(DelftStringUtilities.repeat(originalString, times)).isEqualTo(expectedString);
	}

	static Stream<Arguments> generator() {
		Arguments tc1 = Arguments.of("str null", null, 2, null);
		Arguments tc2 = Arguments.of("empty string", "", 2, "");
		Arguments tc3 = Arguments.of("repeat negative", "x", -1, "");
		Arguments tc4 = Arguments.of("zero repetitions", "x", 0, "");
		Arguments tc5 = Arguments.of("1 char, 1 repetition", "x", 1, "x");
		Arguments tc6 = Arguments.of("2 chars, 1 repetition", "xx", 1, "xx");
		Arguments tc7 = Arguments.of("3+ chars, 1 repetition", "xxx", 1, "xxx");
		Arguments tc8 = Arguments.of("1 char, N repetitions", "x", 3, "xxx");
		Arguments tc9 = Arguments.of("2 chars, N repetitions", "xx", 3, "xxxxxx");
		Arguments tc10 = Arguments.of("3+ chars, N repetitions", "xxx", 3, "xxxxxxxxx");
		return Stream.of(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9, tc10);
	}

	@Test
	void padLimit() {
		assertThat(DelftStringUtilities.repeat("x", 8192)).hasSize(8192);
		assertThat(DelftStringUtilities.repeat("x", 8193)).hasSize(8193);
	}
}
