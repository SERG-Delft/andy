package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class DelftWordUtilitiesTest {

	@ParameterizedTest
	@MethodSource("generator")
	void swapCase(String description, String originalStr, String expectedStr) {
		assertThat(DelftWordUtilities.swapCase(originalStr)).isEqualTo(expectedStr);
	}

	static Stream<Arguments> generator() {
		Arguments tc1 = Arguments.of("str null", null, null);
		Arguments tc2 = Arguments.of("str empty", "", "");
		Arguments tc3 = Arguments.of("1 char upper", "A", "a");
		Arguments tc4 = Arguments.of("1 char lower", "b", "B");
		Arguments tc5 = Arguments.of("1 char whitespace", " ", " ");
		Arguments tc6 = Arguments.of("multiple chars upper", "ABC", "abc");
		Arguments tc7 = Arguments.of("multiple chars lower", "def", "DEF");
		Arguments tc8 = Arguments.of("multiple chars whitespace", "  ", "  ");
		Arguments tc9 = Arguments.of("lower case after whitespace", " k", " K");
		Arguments tc10 = Arguments.of("mutliple chars mixed", "AkdLc keCtk", "aKDlC KEcTK");
		return Stream.of(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9, tc10);
	}
}
