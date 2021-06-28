package nl.tudelft.cse1110.grader.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikArbitrariesTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "JQWikUsingArbitraries,integers,true",
            "JQWikUsingArbitraries,doubles,true",
            "JQWikUsingArbitraries,strings,false",
            "ManyJQWikTests,integers,false"
    })
    void useOfArbitraries(String fixtureName, String arbitraryName, boolean expectation) {
        Check check = new JQWikArbitraries(Arrays.asList(arbitraryName));
        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
