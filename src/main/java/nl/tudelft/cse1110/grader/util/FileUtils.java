package nl.tudelft.cse1110.grader.util;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUtils {

    public static List<String> filePathsAsString(Collection<File> files) {
        return files.stream()
                .map(filePath -> filePath.getAbsolutePath())
                .collect(Collectors.toList());
    }

    public static Collection<File> getAllJavaFiles(String sourceDir) {
        Collection<File> files = org.apache.commons.io.FileUtils.listFiles(
                new File(sourceDir),
                new RegexFileFilter("^.*\\.java$"),
                DirectoryFileFilter.DIRECTORY
        );

        return files;
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

    public static void moveFile(String sourceFile, String destDir, String destFileName) {
        try {
            Path result = Files.move(Paths.get(sourceFile), Paths.get(destDir, destFileName), REPLACE_EXISTING);
            if(result==null)
                throw new RuntimeException("Fail when moving files");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Path copyFile(String sourceFile, String destDir) {
        File file = new File(sourceFile);
        try {
            Path result = Files.copy(Paths.get(sourceFile), Paths.get(destDir, file.getName()), REPLACE_EXISTING);
            if (result == null)
                throw new RuntimeException("Fail when copying files");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String concatenateDirectories(String dir1, String dir2) {
        return dir1 + (dir1.endsWith("/")?"":"/") + dir2;
    }

    public static String findSolution(String workdir) {
        return getAllJavaFiles(workdir)
                .stream().filter(x -> x.getAbsolutePath().endsWith("Solution.java"))
                .map(x -> x.getAbsolutePath())
                .findFirst()
                .get();
    }

    public static List<File> getMetaFiles(String workingDir) {
        File[] files = new File(workingDir).listFiles();

        List<File> metaFiles = new ArrayList<>();
        for (File file : files) {
            if (file.isFile() && file.getName().contains("Meta")) {
                metaFiles.add(file);
            }
        }
        return metaFiles;
    }


    public static Path createTemporaryDirectory(String prefix) {
        try {
            Path result = Files.createTempDirectory(prefix);
            if (result == null)
                throw new RuntimeException("Fail when copying files");
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteDirectory(File directory) {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                deleteFile(file);
            } else if (file.isDirectory()) {
                deleteDirectory(file);
            }
        }
        deleteFile(directory);
    }
}
