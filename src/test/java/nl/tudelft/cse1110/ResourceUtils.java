package nl.tudelft.cse1110;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceUtils {

    public static final String LIBS_FOLDER_PATH = "\\grader\\src\\test\\resources\\grader\\libs\\";

    public static String resourceFolder(String path) {
        try {
            return Paths.get(ResourceUtils.class.getResource("/").toURI()).toString() + path;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    //JARs are downloaded in the resources folder, not in the temporary target folder
    //Thus, they need to be downloaded only once
    public static String permanentResourceFolder(){

        Path path = null;
        try {
            path = Path.of(Paths.get(ResourceUtils.class.getResource("/").toURI()).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        path = Paths.get(path.toString(), "../../../");
        path = Paths.get(path.toString(), LIBS_FOLDER_PATH);

        return path.toString();
    }
}
