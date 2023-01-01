package integration;

import nl.tudelft.cse1110.andy.writer.ResultWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvalidConfigurationTest extends IntegrationTestBase {

    private ResultWriter writer;

    @BeforeEach
    public void setup() {
        writer = mock(ResultWriter.class);
    }

    @Override
    protected ResultWriter getWriter() {
        return writer;
    }

    @Test
    void nonzeroWeightButZeroTotal() {
        assertThrows(RuntimeException.class, () -> {
            run("SoftWhereLibrary", "SoftWhereTests", "SoftWhereConfigurationWithZeroTotal");
        });
        verify(writer).uncaughtError(any(), any(RuntimeException.class));
    }
}
