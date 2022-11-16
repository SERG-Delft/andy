package unit.utils;

import nl.tudelft.cse1110.andy.utils.CodeSnippetUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CodeSnippetUtilsTest {

    @ParameterizedTest
    @MethodSource("generator")
    void generateCodeSnippetSimple(List<String> lines, int lineNumber, String expected) {
        String actual = CodeSnippetUtils.generateCodeSnippet(lines, lineNumber);
        assertThat(actual).isEqualTo(expected);
    }

    public static Stream<Arguments> generator() {
        final var testLines = List.of(
                "a",
                "b",
                "c",
                "d",
                "e",
                "f",
                "g",
                "h"
        );

        return Stream.of(
                Arguments.of( // middle of file
                        testLines,
                        5,
                        "    c\n" +
                        "    d\n" +
                        "--> e\n" +
                        "    f\n" +
                        "    g"
                ),
                Arguments.of( // beginning of file
                        testLines,
                        2,
                        "    a\n" +
                        "--> b\n" +
                        "    c\n" +
                        "    d"
                ),
                Arguments.of( // first line
                        testLines,
                        1,
                        "--> a\n" +
                        "    b\n" +
                        "    c"
                ),
                Arguments.of( // end of file
                        testLines,
                        7,
                        "    e\n" +
                        "    f\n" +
                        "--> g\n" +
                        "    h"
                ),
                Arguments.of( // last line
                        testLines,
                        8,
                        "    f\n" +
                        "    g\n" +
                        "--> h"
                ),
                Arguments.of( // indentation
                        List.of(
                                "       a",
                                "      b",
                                "      if(c) {",
                                "          d",
                                "      }",
                                "   f",
                                "g",
                                "                h"
                        ),
                        3,
                        "     a\n" +
                        "    b\n" +
                        "--> if(c) {\n" +
                        "        d\n" +
                        "    }"
                ),
                Arguments.of( // indentation mixed with blank lines
                        List.of(
                                "       a",
                                "",
                                "      if(c) {",
                                "  ",
                                "          d",
                                "      }",
                                "   f",
                                "g",
                                "                h"
                        ),
                        3,
                        "     a\n" +
                        "\n" +
                        "--> if(c) {\n" +
                        "\n" +
                        "        d"
                ),
                Arguments.of( // file boundary with indentation
                        List.of(
                                "  b",
                                "  if(c) {",
                                "      d",
                                "  }",
                                "f",
                                "g"
                        ),
                        2,
                        "    b\n" +
                        "--> if(c) {\n" +
                        "        d\n" +
                        "    }"
                )
        );
    }
}
