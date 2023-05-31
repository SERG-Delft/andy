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

class TaxIncomeTest {

	private final TaxIncome taxIncome = new TaxIncome();

	@Property
	void tax22100max(@ForAll @DoubleRange(min = 0, max = 22100, maxIncluded = false) double income) {
		assertEquals(taxIncome.calculate(income), 0.15 * income, Math.ulp(income));
	}

	@Property
	void tax53500max(@ForAll @DoubleRange(min = 22100, max = 53500, maxIncluded = false) double income) {
		assertEquals(taxIncome.calculate(income), 3315 + 0.28 * (income - 22100), Math.ulp(income));
	}

	@Property
	void tax115000max(@ForAll @DoubleRange(min = 53500, max = 115000, maxIncluded = false) double income) {
		assertEquals(taxIncome.calculate(income), 12107 + 0.31 * (income - 53500), Math.ulp(income));
	}

	@Property
	void tax250000max(@ForAll @DoubleRange(min = 115000, max = 250000, maxIncluded = false) double income) {
		assertEquals(taxIncome.calculate(income), 31172 + 0.36 * (income - 115000), Math.ulp(income));
	}

	@Property
	void tax250000min(@ForAll @DoubleRange(min = 250000) double income) {
		assertEquals(taxIncome.calculate(income), 79772 + 0.396 * (income - 250000), Math.ulp(income));
	}

	@Property
	void invalid(@ForAll @Negative double income) {
		assertEquals(taxIncome.calculate(income), -1, Math.ulp(income));
	}
}
