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

class LeapYearTest {

	@Property
	void multiplesOf400AreLeapYears(@ForAll("multiplesOf400") int year) {
		assertThat(LeapYear.isLeapYear(year)).isTrue();
	}

	@Property
	void multiplesOf100ButNot400AreNotLeapYears(@ForAll("multiplesOf100ButNot400") int year) {
		assertThat(LeapYear.isLeapYear(year)).isFalse();
	}

	@Property
	void multiplesOf4AreLeapYears(@ForAll("multiplesOf4ButNot100Or400") int year) {
		assertThat(LeapYear.isLeapYear(year)).isTrue();
	}

	@Property
	void notDivisibleBy4IsNotLeapYear(@ForAll("notAMultipleOf4") int year) {
		assertThat(LeapYear.isLeapYear(year)).isFalse();
	}

	@Provide
	Arbitrary<Integer> multiplesOf400() {
		return Arbitraries.integers().filter(n -> n % 400 == 0);
	}

	@Provide
	Arbitrary<Integer> multiplesOf100ButNot400() {
		return Arbitraries.integers().filter(n -> n % 100 == 0 && n % 400 > 0);
	}

	@Provide
	Arbitrary<Integer> multiplesOf4ButNot100Or400() {
		return Arbitraries.integers().filter(n -> n % 4 == 0 && n % 100 > 0 && n % 400 > 0);
	}

	@Provide
	Arbitrary<Integer> notAMultipleOf4() {
		return Arbitraries.integers().filter(n -> n % 4 > 0 && n % 100 > 0 && n % 400 > 0);
	}
}
