package delft;

import java.util.Iterator;
import java.util.TreeSet;

class MathArrays {

	/** <code>MathArrays</code> should not normally be instantiated. */
	private MathArrays() {
	}

	/**
	 * Returns an array consisting of the unique values in {@code data}. The return
	 * array is sorted in descending order. Empty arrays are allowed, but null
	 * arrays result in NullPointerException. Infinities are allowed. NaN values are
	 * allowed with maximum sort order - i.e., if there are NaN values in
	 * {@code data}, {@code Double.NaN} will be the first element of the output
	 * array, even if the array also contains {@code Double.POSITIVE_INFINITY}.
	 *
	 * @param data
	 *            array to scan
	 * @return descending list of values included in the input array
	 * @throws NullPointerException
	 *             if data is null
	 * @since 3.6
	 */
	public static double[] unique(double[] data) {
		TreeSet<Double> values = new TreeSet<Double>();
		for (int i = 0; i < data.length; i++) {
			values.add(data[i]);
		}
		final int count = values.size();
		final double[] out = new double[count];
		Iterator<Double> iterator = values.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			out[count - ++i] = iterator.next();
		}
		return out;
	}
}
