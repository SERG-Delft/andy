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

class ChocolateBarsTest {

	private final ChocolateBars chocolateBars = new ChocolateBars();

	@Property
	void onlySmallBars(@ForAll("onlySmall") ChocolateBarsInput input) {
		assertThat(chocolateBars.calculate(input.small, input.big, input.total)).isEqualTo(input.total);
	}

	@Property
	void onlyBigBars(@ForAll("onlyBig") ChocolateBarsInput input) {
		assertThat(chocolateBars.calculate(input.small, input.big, input.total)).isEqualTo(0);
	}

	@Property
	void bothBars(@ForAll("both") ChocolateBarsInput input) {
		assertThat(chocolateBars.calculate(input.small, input.big, input.total))
				.isEqualTo(input.total - Math.min(input.total / 5, input.big) * 5);
	}

	@Property
	void noBars(@ForAll("none") ChocolateBarsInput input) {
		assertThat(chocolateBars.calculate(input.small, input.big, input.total)).isEqualTo(-1);
	}

	static class ChocolateBarsInput {

		int small;

		int big;

		int total;

		public ChocolateBarsInput(int small, int big, int total) {
			this.small = small;
			this.big = big;
			this.total = total;
		}
	}

	@Provide
	private Arbitrary<ChocolateBarsInput> onlySmall() {
		IntegerArbitrary small = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary big = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary total = Arbitraries.integers().greaterOrEqual(0);
		return Combinators.combine(small, big, total).as(ChocolateBarsInput::new)
				.filter(k -> (k.total < 5 || k.big == 0) && k.small >= k.total);
	}

	@Provide
	private Arbitrary<ChocolateBarsInput> onlyBig() {
		IntegerArbitrary small = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary big = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary total = Arbitraries.integers().greaterOrEqual(0);
		return Combinators.combine(small, big, total).as(ChocolateBarsInput::new)
				.filter(k -> k.total <= 5 * k.big && k.total % 5 == 0);
	}

	@Provide
	private Arbitrary<ChocolateBarsInput> both() {
		IntegerArbitrary small = Arbitraries.integers().greaterOrEqual(1);
		IntegerArbitrary big = Arbitraries.integers().greaterOrEqual(1);
		IntegerArbitrary total = Arbitraries.integers().greaterOrEqual(1);
		return Combinators.combine(small, big, total).as(ChocolateBarsInput::new)
				.filter(k -> k.total - k.big * 5 > 0 && k.small >= k.total - k.big * 5);
	}

	@Provide
	private Arbitrary<ChocolateBarsInput> none() {
		IntegerArbitrary small = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary big = Arbitraries.integers().greaterOrEqual(0);
		IntegerArbitrary total = Arbitraries.integers().greaterOrEqual(1);
		return Combinators.combine(small, big, total).as(ChocolateBarsInput::new)
				.filter(k -> k.small < k.total - Math.min(k.total / 5, k.big) * 5);
	}
}
