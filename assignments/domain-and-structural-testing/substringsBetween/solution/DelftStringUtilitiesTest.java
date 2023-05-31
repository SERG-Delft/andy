package delft;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class DelftStringUtilitiesTest {

	@Test
	void nullCloseStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("hello", "e", null)).isNull();
	}

	@Test
	void emptyCloseStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("hello", "e", "")).isNull();
	}

	@Test
	void nullOpenStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("string", null, "n")).isNull();
	}

	@Test
	void emptyOpenStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("hello", "", "l")).isNull();
	}

	@Test
	void nullStringTest() {
		assertThat(DelftStringUtilities.substringsBetween(null, "a", "b")).isNull();
	}

	@Test
	void emptyStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("", "a", "b")).isEmpty();
	}

	@Test
	void closeLargerThanStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("ab", "a", "abc")).isNull();
	}

	@Test
	void openNotPresentInStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("ab", "c", "b")).isNull();
	}

	@Test
	void closeNotPresentInStringTest() {
		assertThat(DelftStringUtilities.substringsBetween("ab", "a", "c")).isNull();
	}

	@Test
	void twoSubstringsTest() {
		assertThat(DelftStringUtilities.substringsBetween("ahellocjoeabyec", "a", "c")).containsExactly("hello", "bye");
	}

	@Test
	void twoSubstringsNextToEachOtherTest() {
		assertThat(DelftStringUtilities.substringsBetween("ahellocabyec", "a", "c")).containsExactly("hello", "bye");
	}

	@Test
	void emptySubstringTest() {
		assertThat(DelftStringUtilities.substringsBetween("ahellocdefacdef", "a", "c")).containsExactly("hello", "");
	}

	@Test
	void longOpenAndCloseTest() {
		assertThat(DelftStringUtilities.substringsBetween("abchellodeftestabcbyedefbctestdefabctestdeghi", "abc", "def")).containsExactly("hello", "bye");
	}
}
