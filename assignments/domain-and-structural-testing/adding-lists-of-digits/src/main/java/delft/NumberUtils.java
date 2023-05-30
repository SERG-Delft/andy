package delft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class NumberUtils {

	private NumberUtils() {
		// Empty constructor
	}

	/**
	 * This method receives two numbers, `left` and `right`, both represented as a
	 * list of digits. It adds these numbers and returns the result also as a list
	 * of digits.
	 *
	 * <p>
	 * For example, if you want to add the numbers 23 and 42, you would need to
	 * create a (left) list with two elements [2,3] and a (right) list with two
	 * elements [4,2]. 23+42 = 65, so the program would produce another list with
	 * two elements [6,5]
	 *
	 * <p>
	 * [2,3] + [4,2] = [6,5]
	 *
	 * <p>
	 * Each element in the left and right lists should be a number from [0-9]. An
	 * IllegalArgumentException is thrown in case this pre-condition does not hold.
	 *
	 * <p>
	 * Leading zeroes are removed from the result, so [0,3] + [2] = [5] (and not
	 * [0,5]).
	 *
	 * @param left
	 *            a list containing the left number. Null returns null, empty means
	 *            0.
	 * @param right
	 *            a list containing the right number. Null returns null, empty means
	 *            0.
	 * @return the sum of left and right, as a list
	 */
	public static List<Integer> add(List<Integer> left, List<Integer> right) {
		// if any is null, return null
		if (left == null || right == null)
			return null;
        // reverse the numbers so that the least significant digit goes to the left.
		ArrayList<Integer> reversedLeft = new ArrayList<>(left);
		Collections.reverse(reversedLeft);
		ArrayList<Integer> reversedRight = new ArrayList<>(right);
		Collections.reverse(reversedRight);
		LinkedList<Integer> result = new LinkedList<>();
		// while there's a digit, keep summing them
		// if there's carry, take the carry into consideration
		int carry = 0;
		for (int i = 0; i < Math.max(reversedLeft.size(), reversedRight.size()); i++) {
			int leftDigit = reversedLeft.size() > i ? reversedLeft.get(i) : 0;
			int rightDigit = reversedRight.size() > i ? reversedRight.get(i) : 0;
			if (leftDigit < 0 || leftDigit > 9 || rightDigit < 0 || rightDigit > 9)
				throw new IllegalArgumentException();
			int sum = leftDigit + rightDigit + carry;
			result.addFirst(sum % 10);
			carry = sum / 10;
		}
        // add leftover carry
		result.addFirst(carry);
        // remove leading zeroes from the result
		while (result.size() > 1 && result.get(0) == 0)
			result.remove(0);
		return result;
	}
}
