package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.utils.ResourceUtils;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class RandomAsciiArtGenerator {


    /**
     * @return String - ASCII art read from .txt file
     */
    public String getRandomAsciiArt() {

        File randomAsciiFile = pickRandomAsciiArtFile();
        return convertAsciiFileToString(randomAsciiFile);

    }


    /** Picks a random ASCII art .txt file from src/main/resources/congrats
     * @return - File containing ASCII art
     */
    private File pickRandomAsciiArtFile() {

        String asciiDirPath = ResourceUtils.resourceFolder("congrats");
        File asciiDir = new File(asciiDirPath);

        File[] listOfAscii = FilesUtils.getAllFiles(asciiDir);

        // Randomly pick one of the .txt files under resources/congrats
        Random random = new Random();
        File randomAsciiFile = listOfAscii[random.nextInt(listOfAscii.length)];

        return randomAsciiFile;
    }


    /**
     * @param asciiFile - File containing ASCII art
     * @return String - ASCII art read from .txt fileA
     */
    private String convertAsciiFileToString(File asciiFile) {

        StringBuilder asciiArt = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(asciiFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                asciiArt.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return asciiArt.toString();
    }







}
