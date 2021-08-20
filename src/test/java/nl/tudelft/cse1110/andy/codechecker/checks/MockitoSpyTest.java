package nl.tudelft.cse1110.andy.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoSpyTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource({"MockitoSpyCalls,true", "MockitoWhenCalls,false", "MockitoVerifyCalls,false"})
    void findCallsToSpy(String classToRun, boolean expectation) {
        Check check = new MockitoSpy();
        run(classToRun + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
