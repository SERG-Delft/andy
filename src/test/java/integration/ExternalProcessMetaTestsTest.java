package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;


public class ExternalProcessMetaTestsTest extends BaseMetaTestsTest {

    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION = "/andy_test_external_process_local_connection.sh";
    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1 =
            "/andy_test_external_process_local_connection_meta_test_pass_1.sh";
    private static final String EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2 =
            "/andy_test_external_process_local_connection_meta_test_pass_2.sh";

    @BeforeAll
    static void copyShellScripts() throws IOException {
        String tmp = getTempDirectory();

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION), """
                echo "initSignal"
                while true; do echo "HTTP/1.1 200 OK\\nContent-Length: 5\\n\\nhello" | nc -l 8086; done
                """);

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1), """
                echo "initSignal"
                while true; do echo "HTTP/1.1 200 OK\\nContent-Length: 3\\n\\nbye" | nc -l 8086; done
                """);

        Files.writeString(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2), """
                echo "initSignal"
                while true; do echo "HTTP/1.1 200 OK\\nContent-Length: 15\\n\\nauf wiedersehen" | nc -l 8086; done
                """);
    }

    private static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @AfterAll
    static void shellCleanup() throws IOException {
        String tmp = getTempDirectory();
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION));
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_1));
        Files.deleteIfExists(Path.of(tmp + EXTERNAL_PROCESS_LOCAL_CONNECTION_META_TEST_PASS_2));
    }

    @Test
    void localConnectionWithMetaTestsTest() {
        assertTimeoutPreemptively(ofSeconds(30), () -> {
            Result result = run(Action.FULL_WITH_HINTS, "EmptyLibrary", "ExternalProcessLocalConnectionSolution",
                    "ExternalProcessLocalConnectionWithMetaTestsConfiguration");

            assertThat(result.hasFailed()).isFalse();
            assertThat(result.hasGenericFailure()).isFalse();

            assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);
            assertThat(result.getMetaTests().getTotalTests()).isEqualTo(5);

            assertThat(result.getMetaTests())
                    .has(passedMetaTest("example of a passing meta test"))
                    .has(passedMetaTest("example of another passing meta test"))
                    .has(failedMetaTest("example of a failing meta test"));
        });
    }
}