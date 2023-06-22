package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class ClumpsTest {

	@MethodSource("generator")
	@ParameterizedTest(name = "{0}")
	void clumpsTest(String name, int[] nums, int res) {
		assertThat(Clumps.countClumps(nums)).isEqualTo(res);
	}

	private static Stream<Arguments> generator() {
		return Stream.of(Arguments.of("null array", null, 0), Arguments.of("empty array", new int[0], 0),
				Arguments.of("array with one element", new int[]{9}, 0),
				Arguments.of("array with multiple elements no clump", new int[]{3, 6, 2, 7, 4, 2}, 0),
				Arguments.of("array with one continuous clump", new int[]{42, 42, 42, 42}, 1),
				Arguments.of("array with multiple clumps", new int[]{1, 1, 3, 0, 0}, 2),
				Arguments.of("array with multiple clumps side-by-side", new int[]{1, 1, 0, 0, 3}, 2));
	}
}
