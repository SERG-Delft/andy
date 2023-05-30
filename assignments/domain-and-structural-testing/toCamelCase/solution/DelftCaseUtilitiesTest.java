package delft;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

class DelftCaseUtilitiesTest {

    private final DelftCaseUtilities delftCaseUtilities = new DelftCaseUtilities();

	@MethodSource("generator")
	@ParameterizedTest(name = "{0}")
	void domainTest(String name, String str, boolean firstLetter, char[] delimiters, String result) {
		assertThat(delftCaseUtilities.toCamelCase(str, firstLetter, delimiters)).isEqualTo(result);
	}

	private static Stream<Arguments> generator() {
		return Stream.of(Arguments.of("null", null, true, new char[]{'.'}, null),
				Arguments.of("empty", "", true, new char[]{'.'}, ""),
				Arguments.of("non-empty single word, capitalize first letter", "aVOcado", true, new char[]{'.'},
						"Avocado"),
				Arguments.of("non-empty single word, not capitalize first letter", "aVOcado", false, new char[]{'.'},
						"avocado"),
				Arguments.of("non-empty single word, capitalize first letter, no delimiters", "aVOcado", true,
						new char[]{}, "Avocado"),
				Arguments.of("non-empty single word, capitalize first letter, single delimiter", "aVOcado", true,
						new char[]{'.'}, "Avocado"),
				Arguments.of("non-empty single word, capitalize first letter, multiple delimiters", "aVOcado", true,
						new char[]{'c', 'd'}, "AvoAO"),
				Arguments.of("non-empty multiple words, capitalize first letter, no delimiters", "aVOcado bAnana", true,
						new char[]{}, "AvocadoBanana"),
				Arguments.of("non-empty multiple words, capitalize first letter, single existing delimiter",
						"aVOcado-bAnana", true, new char[]{'-'}, "AvocadoBanana"),
				Arguments.of("non-empty multiple words, capitalize first letter, single non-existing delimiter",
						"aVOcado bAnana", true, new char[]{'x'}, "AvocadoBanana"),
				Arguments.of("non-empty multiple words, capitalize first letter, multiple existing delimiters",
						"aVOcado bAnana", true, new char[]{' ', 'n'}, "AvocadoBaAA"),
				Arguments.of("non-empty multiple words, capitalize first letter, multiple non-existing delimiters",
						"aVOcado bAnana", true, new char[]{'x', 'y'}, "AvocadoBanana"),
                Arguments.of("delimiters is null", "apple", true, null, "Apple"),
                Arguments.of("only delimiters in word", "apple", true, new char[]{'a', 'p', 'l', 'e'}, "apple"));
	}
}
