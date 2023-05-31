package delft;

class DelftWordUtilities {

    private DelftWordUtilities() {
        // Override default constructor, to prevent it from getting considered in the coverage report.
    }

	// -----------------------------------------------------------------------
	/**
	 * Swaps the case of a String using a word based algorithm.
	 *
	 * <ul>
	 * <li>Upper case character converts to Lower case
	 * <li>Other Lower case character converts to Upper case
	 * </ul>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}. A {@code null}
	 * input String returns {@code null}.
	 *
	 * @param str
	 *            the String to swap case, may be null
	 * @return the changed String, {@code null} if null String input
	 */
	public static String swapCase(final String str) {
		if (DelftWordUtilities.isEmpty(str)) {
			return str;
		}
		final char[] buffer = str.toCharArray();
		boolean whitespace = true;
		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}


	/**
	 * Checks if a CharSequence is empty ("") or null. *
	 *
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * CharSequence. That functionality is available in isBlank().
	 *
	 * @param cs
	 *            the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
	 */
	private static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}
}
