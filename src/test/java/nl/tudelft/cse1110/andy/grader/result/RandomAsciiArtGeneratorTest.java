package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.TestResourceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.File;

public class RandomAsciiArtGeneratorTest {

    private RandomAsciiArtGenerator asciiArtGenerator = new RandomAsciiArtGenerator();


    @Test
    void testConvertFileToString() {

        File asciiArt = new File(TestResourceUtils.resourceFolder("grader/fixtures/Congrats/cake.txt"));
        String result = asciiArtGenerator.convertAsciiFileToString(asciiArt);

        assertThat(result).isEqualTo(
                "     Super congrats!\n" +
                "\n" +
                "       , , , , , ,\n" +
                "       |_|_|_|_|_|\n" +
                "      |~=,=,=,=,=~|\n" +
                "      |~~~~~~~~~~~|\n" +
                "    |~=,=,=,=,=,=,=~|\n" +
                "    |~~~~~~~~~~~~~~~|\n" +
                "  |~=,=,=,=,=,=,=,=,=~|\n" +
                "  |~~~~~~~~~~~~~~~~~~~|\n" +
                "(^^^^^^^^^^^^^^^^^^^^^^^)\n" +
                " `'-------------------'`\n");

    }



}


