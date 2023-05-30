package delft;

class ArrayUtils {

	/** <code>ArrayUtils</code> should not normally be instantiated. */
	private ArrayUtils() {
	}

	/**
	 * Reverses the order of the given array in the given range.
	 *
	 * <p>
	 * This method does nothing for a {@code null} input array.
	 *
	 * @param array
	 *            the array to reverse, may be {@code null}
	 * @param startIndexInclusive
	 *            the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
	 *            (&gt;array.length) results in no change.
	 * @param endIndexExclusive
	 *            elements up to endIndex-1 are reversed in the array. Undervalue
	 *            (&lt; start index) results in no change. Overvalue
	 *            (&gt;array.length) is demoted to array length.
	 * @since 3.2
	 */
	public static void reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
		if (array == null) {
			return;
		}
		int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
		int j = Math.min(array.length, endIndexExclusive) - 1;
		int tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}
}
