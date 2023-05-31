package delft;

class DelftStringUtilities {

	private DelftStringUtilities() {
		// Override default constructor, to prevent it from getting considered in the
		// coverage report.
	}

	/** The maximum size to which the padding constant(s) can expand. */
	private static final int PAD_LIMIT = 8192;

	/**
	 * The empty String {@code ""}.
	 *
	 * @since 2.0
	 */
	public static final String EMPTY = "";

	/**
	 * Returns padding using the specified delimiter repeated to a given length.
	 *
	 * <p>
	 * Note: this method does not support padding with
	 * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode
	 * Supplementary Characters</a> as they require a pair of {@code char}s to be
	 * represented. If you are needing to support full I18N of your applications
	 * consider using {@link #repeat(String, int)} instead.
	 *
	 * @param ch
	 *            character to repeat
	 * @param repeat
	 *            number of times to repeat char, should be positive
	 * @return String with repeated character
	 * @see #repeat(String, int)
	 */
	private static String repeat(final char ch, final int repeat) {
		final char[] buf = new char[repeat];
		for (int i = repeat - 1; i >= 0; i--) {
			buf[i] = ch;
		}
		return new String(buf);
	}

	/**
	 * Repeat a String {@code repeat} times to form a new String.
	 *
	 * @param str
	 *            the String to repeat, may be null
	 * @param repeat
	 *            number of times to repeat str, negative treated as zero
	 * @return a new String consisting of the original String repeated, {@code null}
	 *         if null String input
	 */
	public static String repeat(final String str, final int repeat) {
		if (str == null) {
			return null;
		}
		if (repeat <= 0) {
			return EMPTY;
		}
		final int inputLength = str.length();
		if (repeat == 1 || inputLength == 0) {
			return str;
		}
		if (inputLength == 1 && repeat <= PAD_LIMIT) {
			return repeat(str.charAt(0), repeat);
		}
		final int outputLength = inputLength * repeat;
		switch (inputLength) {
			case 1 :
				return repeat(str.charAt(0), repeat);
			case 2 :
				final char ch0 = str.charAt(0);
				final char ch1 = str.charAt(1);
				final char[] output2 = new char[outputLength];
				for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
					output2[i] = ch0;
					output2[i + 1] = ch1;
				}
				return new String(output2);
			default :
				final StringBuilder buf = new StringBuilder(outputLength);
				for (int i = 0; i < repeat; i++) {
					buf.append(str);
				}
				return buf.toString();
		}
	}
}
