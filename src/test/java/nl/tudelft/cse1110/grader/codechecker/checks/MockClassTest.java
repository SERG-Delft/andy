package nl.tudelft.cse1110.grader.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MockClassTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"List,true", "Set,true", "HashMap,false"})
    void findMocks(String classToMock, boolean expectation) {
        Check check = new MockClass(Arrays.asList(classToMock));
        run("ManyMocks.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
