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
	void isSorted(String description, int[] array, boolean expectedResult) {
		assertThat(ArrayUtils.isSorted(array)).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> generator() {
		Arguments tc0 = Arguments.of("empty", new int[]{}, true);
		Arguments tc1 = Arguments.of("single element", new int[]{1}, true);
		Arguments tc2 = Arguments.of("unsorted", new int[]{1, 3, 2}, false);
		Arguments tc3 = Arguments.of("sorted", new int[]{1, 2, 3}, true);
		Arguments tc4 = Arguments.of("null array", null, true);
        Arguments tc5 = Arguments.of("sorted with doubles", new int[]{1, 1, 2, 2, 3}, true);
        Arguments tc6 = Arguments.of("two elements", new int[]{2, 1}, false);
		return Stream.of(tc0, tc1, tc2, tc3, tc4, tc5, tc6);
	}

}
