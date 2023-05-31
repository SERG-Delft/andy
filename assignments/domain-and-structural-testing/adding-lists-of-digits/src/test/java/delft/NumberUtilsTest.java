package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class NumberUtilsTest {

    // call the method under test: NumberUtils.add(left, right)

	// returns a mutable list of integers
	private static List<Integer> numbers(int... nums) {
		List<Integer> list = new ArrayList<>();
		for (int n : nums)
			list.add(n);
		return list;
	}

	@Test
	void t1() {
		List<Integer> result = NumberUtils.add(numbers(1, 2), numbers(3, 4));

		assertThat(result).containsExactly(4, 6);
	}
}
