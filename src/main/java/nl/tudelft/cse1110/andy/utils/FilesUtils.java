package nl.tudelft.cse1110.andy.utils;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FilesUtils {

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

    public static File[] getAllFiles(File directoryPath) {
        File[] files = directoryPath.listFiles();
        return files;
    }


    public static void createDirIfNeeded(String dir) {
        // inspired by: https://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
        File theDir = new File(dir);
        if (!theDir.exists()){
            theDir.mkdirs();
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

    public static void moveFile(String sourceFile, String destDir, String destFileName) {
        try {
            Path result = Files.move(Paths.get(sourceFile), Paths.get(destDir, destFileName), REPLACE_EXISTING);
            if(result==null)
                throw new RuntimeException("Fail when moving files");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String concatenateDirectories(String dir1, String dir2) {
        return Paths.get(dir1).resolve(dir2).toAbsolutePath().toString();
    }

    public static String findSolution(String workdir) throws FileNotFoundException {
        return getAllJavaFiles(workdir)
                .stream().filter(x -> x.getAbsolutePath().endsWith("Solution.java")
                        || x.getAbsolutePath().contains("Test"))
                .map(x -> x.getAbsolutePath())
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("Solution file does not exist."));
    }

    public static String findLibrary(String workdir) throws FileNotFoundException {
        return getAllJavaFiles(workdir)
                .stream().filter(x -> x.getAbsolutePath().endsWith("Library.java")
                        || (!x.getAbsolutePath().contains("Test") &&
                            !x.getAbsolutePath().endsWith("Solution.java") &&
                            !x.getAbsolutePath().endsWith("Configuration.java")))
                .map(x -> x.getAbsolutePath())
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("Library file does not exist."));
    }

    public static String findConfiguration(String workdir) throws FileNotFoundException {
        return getAllJavaFiles(workdir)
                .stream().filter(x -> x.getAbsolutePath().endsWith("Configuration.java"))
                .map(x -> x.getAbsolutePath())
                .findFirst()
                .orElseThrow(() -> new FileNotFoundException("Configuration file does not exist."));
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

    public static String readFile(File fileToRead) {
        try {
            return Files.readAllLines(fileToRead.toPath()).stream().collect(Collectors.joining("\n"));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void writeToFile(File destinationFile, String content) {
        try {
            Files.writeString(destinationFile.toPath(), content);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Generate a snippet of code from the given file, starting 2 lines before the given line
     * and ending 2 lines after the given line (inclusive), with an arrow pointing towards the line.
     *
     * @param path The path to the source file.
     * @param line The line number to point to.
     * @return A snippet of code with an arrow pointing to the given line.
     */
    public static String generateCodeSnippet(String path, int line) {
        final int surroundingLines = 2;
        final int arrowSize = 4;

        // read file
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // extract relevant lines
        int start = Math.max(0, line - surroundingLines);
        int end = Math.min(lines.size(), line + surroundingLines);
        List<String> linesToShow = lines.subList(start, end + 1);

        // trim spaces at the beginning of the extracted lines
        int spacesToTrim = linesToShow.stream().mapToInt(FilesUtils::getNumberOfLeadingSpaces).min().orElse(0);
        String[] trimmedLines = linesToShow.stream()
                .map(x -> x.substring(spacesToTrim))
                .toArray(String[]::new);

        // add arrow or spaces
        // (prepend "--> " to the line, and "    " to all other lines)
        int relativeLineNumber = line - start;
        for (int i = 0; i < trimmedLines.length; i++) {
            String s = i == relativeLineNumber ?
                    "-".repeat(arrowSize - 2) + "> " :
                    " ".repeat(arrowSize);
            trimmedLines[i] = s + trimmedLines[i];
        }

        return String.join("\n", trimmedLines);
    }

    private static int getNumberOfLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) != ' ') {
            count++;
        }
        return count;
    }
}
