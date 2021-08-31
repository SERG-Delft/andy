package utils;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class TestResourceUtils {

    public static String resourceFolder(String path) {
        try {
            return Paths.get(TestResourceUtils.class.getResource("/").toURI()) + (path.startsWith("/")?"":"/") + path;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
