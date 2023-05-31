package delft;

import static org.assertj.core.api.Assertions.assertThat;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.UniqueElements;

class PalindromeTests {

	@Property
	void addingReverseYieldsPalindrome(@ForAll String str) {
		String palindrome = str + reverse(str);
		assertThat(Palindrome.isPalindrome(palindrome)).isTrue();
	}

	@Property
	void addingReverseWithExtraCharacterInMiddleYieldsPalindrome(@ForAll String str, @ForAll char c) {
		String palindrome = str + c + reverse(str);
		assertThat(Palindrome.isPalindrome(palindrome)).isTrue();
	}

	@Property
	void uniqueStringsShouldNotBePalindromes(@ForAll @Size(min = 2) @UniqueElements char[] chars) {
		String str = new String(chars);
		assertThat(Palindrome.isPalindrome(str)).isFalse();
	}

	@Property
	void caseDoesNotMatter(@ForAll @AlphaChars String str) {
		StringBuilder sb = new StringBuilder(str);
		sb.reverse();
		String reverse = sb.toString();
		String palindrome1 = str + reverse.toUpperCase();
		assertThat(Palindrome.isPalindrome(palindrome1)).isTrue();
		String palindrome2 = str.toUpperCase() + reverse;
		assertThat(Palindrome.isPalindrome(palindrome2)).isTrue();
	}

	static String reverse(String str) {
		StringBuilder sb = new StringBuilder(str);
		sb.reverse();
		return sb.toString();
	}
}
