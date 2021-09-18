package nl.tudelft.cse1110.andy.writer.weblab;

import java.util.List;
import java.util.Random;

import static nl.tudelft.cse1110.andy.utils.ResourceUtils.listOfCongratsFiles;
import static nl.tudelft.cse1110.andy.utils.ResourceUtils.readCongratsFile;

public class RandomAsciiArtGenerator {

    /**
     * @return String - ASCII art read from .txt file
     */
    public String getRandomAsciiArt() {
        List<String> congratsFiles = listOfCongratsFiles();

        // Randomly pick one of the .txt files
        Random random = new Random();
        String randomAsciiFile = congratsFiles.get(random.nextInt(congratsFiles.size()));

        return readCongratsFile(randomAsciiFile);
    }


}
