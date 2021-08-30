package unit.config;

import nl.tudelft.cse1110.andy.config.MetaTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaTestTest {

    @ParameterizedTest
    @MethodSource("insertInLineGenerator")
    void insertInLine(String oldCode, int lineToInsert, String contentToAdd, String expectedResult) {
        MetaTest metaTest = MetaTest.insertAt("some meta test", lineToInsert, contentToAdd);

        String result = metaTest.evaluate(oldCode);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    static Stream<Arguments> insertInLineGenerator() {
        return Stream.of(
            // middle
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nextra line 1\nextra line 2\nline 3\nline 4\nline 5"),

            // first
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 1\nline 2\nline 3\nline 4\nline 5"),

            // first, but 0
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    0,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 1\nline 2\nline 3\nline 4\nline 5"),

            // last line
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    5,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nextra line 1\nextra line 2\nline 5"),

            // end
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    6,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nline 5\nextra line 1\nextra line 2"),

            // index bigger than end
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    7,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nline 5\nextra line 1\nextra line 2")

        );
    }

    @ParameterizedTest
    @MethodSource("withLineReplacementGenerator")
    void withLineReplacement(String oldCode, int start, int end, String replacement, String expectedResult) {
        MetaTest metaTest = MetaTest.withLineReplacement("some meta test", start,end, replacement);

        String result = metaTest.evaluate(oldCode);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    static Stream<Arguments> withLineReplacementGenerator() {
        return Stream.of(
            // middle, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    2, 4,
                    "extra line 1\nextra line 2",
                    "line 1\nextra line 1\nextra line 2\nline 5"),
            // middle, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    2, 4,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "line 1\nextra line 1\nextra line 2\nextra line 3\nextra line 4\nline 5"),
            // beginning, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1, 3,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 4\nline 5"),
            // beginning, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1, 3,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4\nline 4\nline 5"),
            // end, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3, 5,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nextra line 1\nextra line 2"),
            // end, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3, 5,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "line 1\nline 2\nextra line 1\nextra line 2\nextra line 3\nextra line 4")
        );
    }
}
