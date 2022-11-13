package nl.tudelft.cse1110.andy.writer.standard;

import nl.tudelft.cse1110.andy.execution.Context;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static nl.tudelft.cse1110.andy.utils.FilesUtils.findSolution;

public class CodeSnippetGenerator {
    /**
     * Generate a snippet of code from the solution, starting 2 lines before the given line
     * and ending 2 lines after the given line (inclusive), with an arrow pointing towards the line.
     *
     * @param ctx  The context.
     * @param line The line number to point to (1-indexed).
     * @return A snippet of code with an arrow pointing to the given line.
     */
    public String generateCodeSnippetFromSolution(Context ctx, int line) throws FileNotFoundException {
        String solutionPath = findSolution(ctx.getDirectoryConfiguration().getWorkingDir());
        return this.generateCodeSnippet(solutionPath, line);
    }

    /**
     * Generate a snippet of code from the given file, starting 2 lines before the given line
     * and ending 2 lines after the given line (inclusive), with an arrow pointing towards the line.
     *
     * @param path The path to the source file.
     * @param line The line number to point to (1-indexed).
     * @return A snippet of code with an arrow pointing to the given line.
     */
    private String generateCodeSnippet(String path, int line) {
        final int surroundingLines = 2;
        final int arrowSize = 4;

        int lineZeroIndexed = line - 1;

        // read file
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        // extract relevant lines
        int start = Math.max(0, lineZeroIndexed - surroundingLines);
        int end = Math.min(lines.size(), lineZeroIndexed + surroundingLines);
        List<String> linesToShow = lines.subList(start, end + 1);

        // trim spaces at the beginning of the extracted lines
        int spacesToTrim = linesToShow.stream().mapToInt(CodeSnippetGenerator::getNumberOfLeadingSpaces).min().orElse(0);
        String[] trimmedLines = linesToShow.stream()
                .map(x -> x.substring(spacesToTrim))
                .toArray(String[]::new);

        // add arrow or spaces
        // (prepend "--> " to the line, and "    " to all other lines)
        int relativeLineNumber = lineZeroIndexed - start;
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
