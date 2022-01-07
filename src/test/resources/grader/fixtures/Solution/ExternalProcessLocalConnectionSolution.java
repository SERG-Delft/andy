package delft;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ExternalProcessLocalConnectionTests {

    @Test
    void responseTest() throws IOException {
        String tmp = System.getProperty("java.io.tmpdir");
        String str = Files.readString(Path.of(tmp + "/andy_test_external_process_output.txt"));
        assertThat(str).isEqualToIgnoringWhitespace("hello");
    }
}
