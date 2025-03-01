package integration;

import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityTest extends IntegrationTestBase {
    @ParameterizedTest
    @CsvSource({
            "SecurityOtherPackageName,package name of your solution",
            "SecurityInstantiateConfiguration,It is not allowed to use",
            "SecurityUseReflection,It is not allowed to use"
    })
    void failingSecurityCheck(String exploitFile, String expectedMessage) {
        Result result = run(Action.FULL_WITHOUT_HINTS, "EmptyLibrary", exploitFile, "EmptyConfiguration");

        assertThat(result.getCompilation().successful()).isFalse();
        assertThat(result.getCompilation().getErrors())
                .hasSize(1)
                .allMatch(err -> err.getMessage().contains(expectedMessage));
    }
}
