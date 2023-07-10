package unit.codechecker.checks;

import nl.tudelft.cse1110.andy.codechecker.checks.Check;
import nl.tudelft.cse1110.andy.codechecker.checks.ClassUsedInTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassUsedInTestsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "SomeStaticClass, true",
            "SomeClass, true",
            "SomeUselessClass, true",
            "Math, true",
            "CreatedOutside, true"
    })
    void classUsesInTestMethod(String className, boolean expectation) {
        Check check = new ClassUsedInTests(className);
        run("ClassUsed.java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }
}

