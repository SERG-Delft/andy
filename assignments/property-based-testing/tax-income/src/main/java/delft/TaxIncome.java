package delft;

class TaxIncome {

	public static final double CANNOT_CALC_TAX = -1;

	public double calculate(double income) {
		if (0 <= income && income < 22100) {
			return 0.15 * income;
		} else if (22100 <= income && income < 53500) {
			return 3315 + 0.28 * (income - 22100);
		} else if (53500 <= income && income < 115000) {
			return 12107 + 0.31 * (income - 53500);
		} else if (115000 <= income && income < 250000) {
			return 31172 + 0.36 * (income - 115000);
		} else if (250000 <= income) {
			return 79772 + 0.396 * (income - 250000);
		}
		return CANNOT_CALC_TAX;
	}
}
