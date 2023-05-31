package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class MathArraysTest {

	@ParameterizedTest(name = "{0}")
	@MethodSource("generator")
	void concatenate(String description, double[][] arrays, double[] expectedOutput) {
		double[] output = MathArrays.concatenate(arrays);
		assertThat(output).isEqualTo(expectedOutput);
	}

	@Test
	void nullArray() {
		assertThatThrownBy(() -> MathArrays.concatenate(null)).isInstanceOf(NullPointerException.class);
		assertThatThrownBy(() -> MathArrays.concatenate(doubles(1), null)).isInstanceOf(NullPointerException.class);
	}

	private static Stream<Arguments> generator() {
		Arguments tc1 = Arguments.of("single array", doublesOf(doubles(1d, 2d, 3d)), doubles(1d, 2d, 3d));
		Arguments tc2 = Arguments.of("two arrays", doublesOf(doubles(1d, 2d, 3d), doubles(4d, 5d, 6d)),
				doubles(1d, 2d, 3d, 4d, 5d, 6d));
		Arguments tc3 = Arguments.of("multiple arrays", doublesOf(doubles(1d, 2d, 3d), doubles(4d, 5d), doubles(6d)),
				doubles(1d, 2d, 3d, 4d, 5d, 6d));
		Arguments tc4 = Arguments.of("empty array", doublesOf(doubles(1), new double[]{}), doubles(1));
		Arguments tc5 = Arguments.of("no arrays", doublesOf(), doubles());
		return Stream.of(tc1, tc2, tc3, tc4, tc5);
	}

	private static double[] doubles(double... ds) {
		return ds;
	}

	private static double[][] doublesOf(double[]... ds) {
		return ds;
	}
}
