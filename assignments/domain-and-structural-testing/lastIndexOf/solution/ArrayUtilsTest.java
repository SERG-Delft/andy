package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ArrayUtilsTest {

	public static final int INDEX_NOT_FOUND = -1;

	@ParameterizedTest
	@MethodSource("generator")
	void lastIndexOf(String description, int[] array, int valueToFind, int startIndex, int expectedResult) {
		assertThat(ArrayUtils.lastIndexOf(array, valueToFind, startIndex)).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> generator() {
		Arguments tc1 = Arguments.of("T1 null array", null, 1, 2, INDEX_NOT_FOUND);
		Arguments tc2 = Arguments.of("T2 negative index", new int[]{0, 1, 2}, 2, -1, INDEX_NOT_FOUND);
		Arguments tc3 = Arguments.of("T3 index bigger than array", new int[]{0, 1, 2}, 1, 5, 1);
		Arguments tc4 = Arguments.of("T4 empty array", new int[]{}, 0, 1, INDEX_NOT_FOUND);
		Arguments tc5 = Arguments.of("T5 length one array with element", new int[]{1}, 1, 0, 0);
		Arguments tc6 = Arguments.of("T6 length one array without element", new int[]{1}, 2, 0, INDEX_NOT_FOUND);
		Arguments tc7 = Arguments.of("T7 array with element", new int[]{0, 1, 2}, 1, 2, 1);
		Arguments tc8 = Arguments.of("T8 array with element many times", new int[]{0, 1, 1, 2}, 1, 2, 2);
		Arguments tc9 = Arguments.of("T9 array without element", new int[]{0, 1, 2}, 3, 2, INDEX_NOT_FOUND);
		Arguments tc10 = Arguments.of("T10 array with element, start index == 0", new int[]{0, 1, 2}, 0, 0, 0);
		Arguments tc11 = Arguments.of("T11 array without element, start index == 0", new int[]{0, 1, 2}, 3, 0,
				INDEX_NOT_FOUND);
		Arguments tc12 = Arguments.of("T12 element at the index", new int[]{0, 1, 1, 2}, 1, 1, 1);
		return Stream.of(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9, tc10, tc11, tc12);
	}
}
