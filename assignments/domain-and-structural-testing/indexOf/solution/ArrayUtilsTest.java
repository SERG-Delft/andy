package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ArrayUtilsTest {

	@ParameterizedTest
	@MethodSource("generator")
	void indexOf(String description, int[] arr, int valueToFind, int startIndex, int expected) {
		int result = ArrayUtils.indexOf(arr, valueToFind, startIndex);
		assertThat(result).isEqualTo(expected);
	}

	private static Stream<Arguments> generator() {
		return Stream.of(Arguments.of("array is null", null, 0, 0, -1),
				Arguments.of("value not in array", new int[]{1, 5, 7, 3}, 6, 0, -1),
				Arguments.of("negative start index", new int[]{3, 7, 4, 6}, 4, -5, 2),
				Arguments.of("startValue before valueToFind", new int[]{1, 2, 3, 4}, 4, 1, 3),
				Arguments.of("startIndex exactly valueToFind", new int[]{1, 3, 4, 5}, 3, 1, 1),
				Arguments.of("startIndex after valueToFind", new int[]{1, 2, 3, 4, 5}, 3, 3, -1),
				Arguments.of("startIndex larger than array length", new int[]{1, 2}, 2, 3, -1),
				Arguments.of("index exactly array.length-1", new int[]{1, 2, 3}, 3, 2, 2),
				Arguments.of("index exactly array.length-1", new int[]{1, 2, 3}, 2, 2, -1),
				Arguments.of("multiple ocurrences", new int[]{1, 2, 3, 2, 2, 2, 2, 2}, 2, 0, 1),
				Arguments.of("value is the first element", new int[]{1, 5, 7, 3}, 1, 0, 0),
				Arguments.of("value is the first element and index is negative", new int[]{1, 5, 7, 3}, 1, -10, 0),
				Arguments.of("empty array", new int[]{}, 0, 0, -1));
	}
}
