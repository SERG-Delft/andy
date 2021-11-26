package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

@EnabledOnOs({OS.LINUX, OS.MAC})
public class SeleniumTest extends IntegrationTestBase {

    @Test
    void seleniumTest() {
        assertTimeoutPreemptively(ofSeconds(30), () -> {
            Result result = run(Action.TESTS, "EmptyLibrary", "SeleniumOnePassingOneFailingSolution",
                    "SeleniumSimpleWebpageConfiguration");

            assertThat(result.hasGenericFailure()).isFalse();
            assertThat(result.getTests().getTestsRan()).isEqualTo(2);
            assertThat(result.getTests().getTestsSucceeded()).isEqualTo(1);
        });
    }
}
