package nl.tudelft.cse1110.andy.utils;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ResourceUtils {

    public static String resourceFolder(String path) {
        try {
            return ResourceUtils.class.getResource((path.startsWith("/") ? "" : "/") + path).getPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> listOfCongratsFiles() {
        try {
            InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream("/congrats/0list.txt");
            String list = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);

            List<String> allFiles = Arrays.asList(list.split("\n"));

            if(allFiles.isEmpty())
                throw new RuntimeException("No congrats files!");

            return allFiles;

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String readCongratsFile(String randomAsciiFile) {
        try {
            InputStream resourceAsStream = ResourceUtils.class.getResourceAsStream("/congrats/" + randomAsciiFile);
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }
}
