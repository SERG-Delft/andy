package nl.tudelft.cse1110;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {

    public static String resourceFolder(String path) {
        try {
            return Paths.get(ResourceUtils.class.getResource("/").toURI()).toString() + path;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}