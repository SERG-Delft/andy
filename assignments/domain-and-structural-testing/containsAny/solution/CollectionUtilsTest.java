package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class CollectionUtilsTest {

	@ParameterizedTest(name = "{0}")
	@MethodSource("generator")
	void containsAny(String description, Collection<?> coll1, Collection<?> coll2, boolean expectedResult) {
		boolean result = CollectionUtils.containsAny(coll1, coll2);
		assertThat(result).isEqualTo(expectedResult);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("nullGenerator")
	void nullList(String description, Collection<?> coll1, Collection<?> coll2) {
		assertThatThrownBy(() -> CollectionUtils.containsAny(coll1, coll2)).isInstanceOf(Exception.class);
	}

	private static Stream<Arguments> generator() {
		// many elements in both
		Arguments tc1 = Arguments.of("many elements in c1, c2, single intersection", Arrays.asList(1, 2, 3),
				Arrays.asList(4, 1, 5), true);
		Arguments tc2 = Arguments.of("many elements in c1, c2, >1 intersection", Arrays.asList(1, 2, 3),
				Arrays.asList(1, 4, 2), true);
		Arguments tc3 = Arguments.of("many elements in c1, c2, no intersection", Arrays.asList(1, 2, 3),
				Arrays.asList(4, 5, 6), false);
		// single element in c1
		Arguments tc4 = Arguments.of("single element in c1, many in c2, intersection", Arrays.asList(1),
				Arrays.asList(1, 4, 5), true);
		Arguments tc5 = Arguments.of("single element in c1, many in c2, no intersection", Arrays.asList(1),
				Arrays.asList(4, 5, 6), false);
		// single element in c2
		Arguments tc6 = Arguments.of("many elements in c1, single in c2, intersection", Arrays.asList(1, 4, 5),
				Arrays.asList(1), true);
		Arguments tc7 = Arguments.of("many elements in c1, single in c2, no intersection", Arrays.asList(4, 5, 6),
				Arrays.asList(1), false);
		// empty lists
		Arguments tc8 = Arguments.of("empty c1", new ArrayList<Integer>(), Arrays.asList(1), false);
		Arguments tc9 = Arguments.of("empty c2", Arrays.asList(1), Collections.emptyList(), false);
		Arguments tc10 = Arguments.of("empty c1 and c2", Collections.emptyList(), Collections.emptyList(), false);
		return Stream.of(tc1, tc2, tc3, tc4, tc5, tc6, tc7, tc8, tc9, tc10);
	}

	private static Stream<Arguments> nullGenerator() {
		Arguments tc1 = Arguments.of("c1 null", null, Arrays.asList(1, 4, 5));
		Arguments tc2 = Arguments.of("c2 null", Arrays.asList(1, 2, 3), null);
		Arguments tc3 = Arguments.of("both null", null, null);
		return Stream.of(tc1, tc2, tc3);
	}
}
