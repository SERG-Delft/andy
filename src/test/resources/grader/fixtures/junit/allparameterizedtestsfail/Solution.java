package tudelft.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tudelft.domain.PassingGrade;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// in all test cases, the expected booleans should be flipped.
class PassingGradeTest {

    @ParameterizedTest
    @MethodSource("generator")
    void passed(float grade, boolean pass) {
        boolean result = new PassingGrade().passed(grade);
        assertEquals(pass, result);
    }

    private static Stream<Arguments> generator() {
        return Stream.of(
                Arguments.of(1.0f, true), // out-point
                Arguments.of(4.9f, true), // boundary: off-point
                Arguments.of(5.0f, false), // boundary: on-point
                Arguments.of(10.0f, false), // boundary: max value
                Arguments.of(7.5f, false)    // extra in-point (small test suite)
        );
    }
}