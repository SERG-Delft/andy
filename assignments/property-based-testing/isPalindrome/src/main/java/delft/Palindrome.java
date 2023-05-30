package delft;

class Palindrome {

	private Palindrome() {
		// Empty constructor
	}

	/**
	 * Returns whether the given word is a palindrome. A word is a palindrome if the
	 * result of reading it from left to right is the same as that of reading it
	 * from right to left.
	 */
	public static boolean isPalindrome(String word) {
		for (int i = 0; i < word.length() / 2; i++) {
			int indexFromEnd = word.length() - 1 - i;
			if (!word.substring(i, i + 1).equalsIgnoreCase(word.substring(indexFromEnd, indexFromEnd + 1))) {
				return false;
			}
		}
		return true;
	}
}
