package nl.tudelft.cse1110.grader.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CompilationUtilsTest {

    @Test
    void checkMissingImport_InDictionary() {
        String diagnostic = "cannot find symbol\n" +
                "  symbol:   class List\n" +
                "  location: class delft.ArrayUtilsTests";
        Optional<String> output = CompilationUtils.checkMissingImport(diagnostic);
        assertThat(output).isPresent();
        assertThat(output.get()).isEqualTo("Maybe you missed the import for List?\n" +
                "Try adding this: import java.util.List;\n");
    }

    @Test
    void checkMissingImport_NotInDictionary() {
        String diagnostic = "cannot find symbol\n" +
                "  symbol:   class Tralala\n" +
                "  location: class delft.ArrayUtilsTests";

        Optional<String> output = CompilationUtils.checkMissingImport(diagnostic);
        assertThat(output).isEmpty();
    }
}