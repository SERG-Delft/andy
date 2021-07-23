package nl.tudelft.cse1110;

import java.net.URISyntaxException;

public class ResourceUtils {

    public static String resourceFolder(String path) {
        try {
            return ResourceUtils.class.getResource(path).toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
