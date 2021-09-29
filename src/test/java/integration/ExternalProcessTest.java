package integration;

import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@EnabledOnOs({OS.LINUX, OS.MAC})
public class ExternalProcessTest extends IntegrationTestBase {

    private static final String END_SIGNAL_SHELL_SCRIPT_PATH = "/andy_test_external_process_end_signal.sh";
    private static final String GRACEFUL_EXIT_SHELL_SCRIPT_PATH = "/andy_test_external_process_graceful_exit.sh";

    private static final String END_SIGNAL_GENERATED_FILE_PATH = "/andy_test_external_process_generated";
    private static final String END_SIGNAL_GENERATED_FILE_PATH_2 = "/andy_test_external_process_generated_2";
    private static final String GRACEFUL_EXIT_GENERATED_FILE_PATH = "/andy_test_external_process_generated";

    @BeforeAll
    static void copyShellScripts() throws IOException {
        String tmp = getTempDirectory();

        Files.writeString(Path.of(tmp + END_SIGNAL_SHELL_SCRIPT_PATH), String.format("""
                        echo "hello" > %s
                        echo "endsignal"
                        sleep 30
                        echo "should be killed before this line is executed" > %s
                        """,
                tmp + END_SIGNAL_GENERATED_FILE_PATH,
                tmp + END_SIGNAL_GENERATED_FILE_PATH_2));

        Files.writeString(Path.of(tmp + GRACEFUL_EXIT_SHELL_SCRIPT_PATH), String.format("""
                echo "hello" > %s
                """, tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH));
    }

    @AfterAll
    static void shellCleanup() throws IOException {
        String tmp = getTempDirectory();
        Files.deleteIfExists(Path.of(tmp + END_SIGNAL_SHELL_SCRIPT_PATH));
        Files.deleteIfExists(Path.of(tmp + GRACEFUL_EXIT_SHELL_SCRIPT_PATH));
        Files.deleteIfExists(Path.of(tmp + END_SIGNAL_GENERATED_FILE_PATH));
        Files.deleteIfExists(Path.of(tmp + END_SIGNAL_GENERATED_FILE_PATH_2));
        Files.deleteIfExists(Path.of(tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH));
    }

    private static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    @Test
    void externalProcessGracefulExit() {
        Result result = run("EmptyLibrary", "EmptySolution",
                "ExternalProcessGracefulExitConfiguration");

        assertThat(result.getGenericFailure()).isNull();

        String tmp = getTempDirectory();
        assertThat(new File(tmp + GRACEFUL_EXIT_GENERATED_FILE_PATH)).exists();
    }

    @Test
    void externalProcessEndSignal() {
        assertTimeoutPreemptively(ofSeconds(10), () -> {

            Result result = run("EmptyLibrary", "EmptySolution",
                    "ExternalProcessEndSignalConfiguration");

            assertThat(result.getGenericFailure()).isNull();

            String tmp = getTempDirectory();
            assertThat(new File(tmp + END_SIGNAL_GENERATED_FILE_PATH)).exists();
            assertThat(new File(tmp + END_SIGNAL_GENERATED_FILE_PATH_2)).doesNotExist();

        });
    }
}
