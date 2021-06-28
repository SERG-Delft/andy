package nl.tudelft.cse1110.grader.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUtils {

    public static List<File> asFiles(List<String> files) {
        return files.stream()
                .map(filePath -> new File(filePath))
                .collect(Collectors.toList());
    }

    public static List<String> getAllJavaFiles(String sourceDir) {
        List<String> result = new ArrayList<>();

        File directoryPath = new File(sourceDir);
        File filesList[] = directoryPath.listFiles();
        for(File file : filesList) {
            if(file.getName().endsWith("java"))
                result.add(file.getAbsolutePath());
        }

        return result;
    }

    public static void createDirIfNeeded(String dir) {
        // inspired by: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
        File theDir = new File(dir);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
    }

    public static void moveClass(String sourceDir, String className, String destDir) {
        try {
            Path result = Files.move(Paths.get(sourceDir, className + ".class"), Paths.get(destDir, className + ".class"), REPLACE_EXISTING);
            if(result==null)
                throw new RuntimeException("Fail when moving files");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyFile(String sourceFile, String destDir, String destFileName) {
        try {
            Path result = Files.copy(Paths.get(sourceFile), Paths.get(destDir, destFileName), REPLACE_EXISTING);
            if(result==null)
                throw new RuntimeException("Fail when moving files");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String concatenateDirectories(String dir1, String dir2) {
        return dir1 + (dir1.endsWith("/")?"":"/") + dir2;
    }
}
