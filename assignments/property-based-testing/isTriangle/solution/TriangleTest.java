package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import net.jqwik.api.*;
import net.jqwik.api.arbitraries.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class TriangleTest {

	@Property
	void triangleIsInvalidIfOneSideIsBiggerThanOthers(@ForAll("invalidTrianglesGenerator") ABC abc) {
		assertThat(Triangle.isTriangle(abc.a, abc.b, abc.c)).isFalse();
	}

	@Provide
	Arbitrary<ABC> invalidTrianglesGenerator() {
		Arbitrary<Integer> normalSide1 = Arbitraries.integers();
		Arbitrary<Integer> normalSide2 = Arbitraries.integers();
		Arbitrary<Integer> biggerSide = Arbitraries.integers();
		Arbitrary<ABC> biggerA = Combinators.combine(normalSide1, normalSide2, biggerSide).as(ABC::new)
				.filter(k -> k.a >= k.b + k.c);
		Arbitrary<ABC> biggerB = Combinators.combine(normalSide1, normalSide2, biggerSide).as(ABC::new)
				.filter(k -> k.b >= k.a + k.c);
		Arbitrary<ABC> biggerC = Combinators.combine(normalSide1, normalSide2, biggerSide).as(ABC::new)
				.filter(k -> k.c >= k.a + k.b);
		return Arbitraries.oneOf(biggerA, biggerB, biggerC);
	}

	@Property
	void triangleIsValidOtherwise(@ForAll("validTriangleGenerator") ABC abc) {
		assertThat(Triangle.isTriangle(abc.a, abc.b, abc.c)).isTrue();
	}

	@Provide
	Arbitrary<ABC> validTriangleGenerator() {
		Arbitrary<Integer> normalSide1 = Arbitraries.integers();
		Arbitrary<Integer> normalSide2 = Arbitraries.integers();
		Arbitrary<Integer> normalSide3 = Arbitraries.integers();
		return Combinators.combine(normalSide1, normalSide2, normalSide3).as(ABC::new)
				.filter(k -> (k.a < k.b + k.c) && (k.b < k.a + k.c) && (k.c < k.a + k.b));
	}

	// use the ABC class below.
	class ABC {

		int a;

		int b;

		int c;

		public ABC(int a, int b, int c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
	}
}
