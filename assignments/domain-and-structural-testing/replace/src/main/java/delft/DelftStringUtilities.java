package delft;

class DelftStringUtilities extends DelftStringUtilitiesExtra {

	private DelftStringUtilities() {
		// Empty constructor
	}

	/**
	 * Replaces a String with another String inside a larger String, for the first
	 * {@code max} values of the search String, case sensitively/insensisitively
	 * based on {@code ignoreCase} value.
	 *
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 *
	 * @param text
	 *            text to search and replace in, may be null
	 * @param searchString
	 *            the String to search for (case insensitive), may be null
	 * @param replacement
	 *            the String to replace it with, may be null
	 * @param max
	 *            maximum number of values to replace, or {@code -1} if no maximum
	 * @param ignoreCase
	 *            if true replace is case insensitive, otherwise case sensitive
	 * @return the text with any replacements processed, {@code null} if null String
	 *         input
	 */
	public static String replace(final String text, String searchString, final String replacement, int max,
			final boolean ignoreCase) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		if (ignoreCase) {
			searchString = searchString.toLowerCase();
		}
		int start = 0;
		int end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return text;
		}
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(text, start, end).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
		}
		buf.append(text, start, text.length());
		return buf.toString();
	}
}

class DelftStringUtilitiesExtra {

	public static final int INDEX_NOT_FOUND = -1;

	public static boolean isEmpty(final CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int startPos) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (startPos < 0) {
			startPos = 0;
		}
		final int endLimit = str.length() - searchStr.length() + 1;
		if (startPos > endLimit) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return startPos;
		}
		for (int i = startPos; i < endLimit; i++) {
			if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
			final CharSequence substring, final int start, final int length) {
		if (cs instanceof String && substring instanceof String) {
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
		}
		int index1 = thisStart;
		int index2 = start;
		int tmpLen = length;
		// Extract these first so we detect NPEs the same as the java.lang.String
		// version
		final int srcLen = cs.length() - thisStart;
		final int otherLen = substring.length() - start;
		// Check for invalid parameters
		if (thisStart < 0 || start < 0 || length < 0) {
			return false;
		}
		// Check that the regions are long enough
		if (srcLen < length || otherLen < length) {
			return false;
		}
		while (tmpLen-- > 0) {
			final char c1 = cs.charAt(index1++);
			final char c2 = substring.charAt(index2++);
			if (c1 == c2) {
				continue;
			}
			if (!ignoreCase) {
				return false;
			}
			// The same check as in String.regionMatches():
			if (Character.toUpperCase(c1) != Character.toUpperCase(c2)
					&& Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
				return false;
			}
		}
		return true;
	}

	static int indexOf(final CharSequence cs, final CharSequence searchChar, final int start) {
		return cs.toString().indexOf(searchChar.toString(), start);
		// if (cs instanceof String && searchChar instanceof String) {
		// // TODO: Do we assume searchChar is usually relatively small;
		// // If so then calling toString() on it is better than reverting to
		// // the green implementation in the else block
		// return ((String) cs).indexOf((String) searchChar, start);
		// } else {
		// // TODO: Implement rather than convert to String
		// return cs.toString().indexOf(searchChar.toString(), start);
		// }
	}
}
