package delft;

class MathArrays {

	/** <code>MathArrays</code> should not normally be instantiated. */
	private MathArrays() {
	}

	/**
	 * Concatenates a sequence of arrays. The return array consists of the entries
	 * of the input arrays concatenated in the order they appear in the argument
	 * list. Null arrays cause NullPointerExceptions; zero length arrays are allowed
	 * (contributing nothing to the output array).
	 *
	 * @param x
	 *            list of double[] arrays to concatenate
	 * @return a new array consisting of the entries of the argument arrays
	 * @throws NullPointerException
	 *             if any of the arrays are null
	 * @since 3.6
	 */
	public static double[] concatenate(double[]... x) {
		int combinedLength = 0;
		for (double[] a : x) {
			combinedLength += a.length;
		}
		int offset = 0;
		int curLength = 0;
		final double[] combined = new double[combinedLength];
		for (int i = 0; i < x.length; i++) {
			curLength = x[i].length;
			System.arraycopy(x[i], 0, combined, offset, curLength);
			offset += curLength;
		}
		return combined;
	}
}
