package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ArrayUtilsTest {

	@ParameterizedTest(name = "{0}")
	@MethodSource("generator")
	void reverse(String description, int[] arrayOfInts, int startIndex, int endIndex, int[] expectedArray) {
		ArrayUtils.reverse(arrayOfInts, startIndex, endIndex);
		assertThat(arrayOfInts).isEqualTo(expectedArray);
	}

	private static Stream<Arguments> generator() {
		Arguments tc1 = Arguments.of("startIndex < endIndex", ints(1, 2, 3, 4, 5), 1, 4, ints(1, 4, 3, 2, 5));
		Arguments tc2 = Arguments.of("startIndex > endIndex", ints(1, 2, 3, 4, 5), 4, 1, ints(1, 2, 3, 4, 5));
		Arguments tc3 = Arguments.of("startIndex == endIndex", ints(1, 2, 3, 4, 5), 1, 1, ints(1, 2, 3, 4, 5));
		Arguments tc4 = Arguments.of("startIndex == endIndex + 1", ints(1, 2, 3, 4, 5), 1, 2, ints(1, 2, 3, 4, 5));
		Arguments tc5 = Arguments.of("startIndex == endIndex + 2", ints(1, 2, 3, 4, 5), 1, 3, ints(1, 3, 2, 4, 5));
		Arguments tc6 = Arguments.of("startIndex zero", ints(1, 2, 3, 4, 5), 0, 4, ints(4, 3, 2, 1, 5));
		Arguments tc7 = Arguments.of("endIndex zero", ints(1, 2, 3, 4, 5), 0, 0, ints(1, 2, 3, 4, 5));
		Arguments tc8 = Arguments.of("endIndex larger than array size", ints(1, 2, 3, 4, 5), 1, 10,
				ints(1, 5, 4, 3, 2));
		Arguments tc9 = Arguments.of("endIndex precisely array size", ints(1, 2, 3, 4, 5), 1, 5, ints(1, 5, 4, 3, 2));
		Arguments tc10 = Arguments.of("endIndex last index", ints(1, 2, 3, 4, 5), 1, 4, ints(1, 4, 3, 2, 5));
		Arguments tc11 = Arguments.of("null array", null, 0, 1, null);
		Arguments tc12 = Arguments.of("negative start index", ints(1, 2, 3, 4, 5), -1, 3, ints(3, 2, 1, 4, 5));
		Arguments tc13 = Arguments.of("negative end index", ints(1, 2, 3, 4, 5), 1, -1, ints(1, 2, 3, 4, 5));
		Arguments tc14 = Arguments.of("startIndex larger than array size", ints(1, 2, 3, 4, 5), 10, 3,
				ints(1, 2, 3, 4, 5));
		Arguments tc15 = Arguments.of("empty array", ints(), 0, 0, ints());
		return Stream.of(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9, tc10, tc11, tc12, tc13, tc14, tc15);
	}

	private static int[] ints(int... nums) {
		return nums;
	}
}
