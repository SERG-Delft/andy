package integration;

import nl.tudelft.cse1110.andy.writer.ResultWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@Disabled
public class InvalidConfigurationTest extends IntegrationTestBase {

    @Test
    void nonzeroWeightButZeroTotal() {
        assertThrows(RuntimeException.class, () -> {
            run("SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigurationWithZeroTotal");
        });
    }
}
