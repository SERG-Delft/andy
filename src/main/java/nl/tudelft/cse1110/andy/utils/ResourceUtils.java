package nl.tudelft.cse1110.andy.utils;

import net.bytebuddy.asm.Advice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ResourceUtils {

    public static String resourceFolder(String path) {
        try {

            return Paths.get("src/main/resources/")
                    + (path.startsWith("/")?"":"/") + path;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
