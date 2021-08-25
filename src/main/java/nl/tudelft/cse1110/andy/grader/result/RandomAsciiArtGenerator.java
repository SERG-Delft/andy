package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.ResourceUtils;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class RandomAsciiArtGenerator {


    public File pickRandomAsciiArt() {

        String asciiDirPath = ResourceUtils.resourceFolder("congrats");
        File asciiDir = new File(asciiDirPath);

        File[] listOfAscii = FilesUtils.getAllFiles(asciiDir);

        // Randomly pick one of the .txt files under resources/congrats
        Random random = new Random();
        File randomAsciiFile = listOfAscii[random.nextInt(listOfAscii.length)];

        return randomAsciiFile;
    }



}
