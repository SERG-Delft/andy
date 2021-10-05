package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityTest extends IntegrationTestBase {

    @ParameterizedTest
    @CsvSource({
            "WriteResultsXml,results.xml write",
            "SystemExit,exitVM.",
            "SetProperty,test write",
            "RuntimeExec,execute",
            "ReadConfiguration,Configuration.java read"
    })
    void securityTest(String exploitFile, String expectedMessage) {
        Result result = run(Action.TESTS, "EmptyLibrary", "securitytests/" + exploitFile, "EmptyConfiguration");

        assertThat(result.getTests().getFailures().get(0).getMessage())
                .startsWith(SecurityException.class.getName())
                .contains("Operation not permitted")
                .contains(expectedMessage);
    }

}
