package nl.tudelft.cse1110.andy.utils;

import java.util.List;

public class CodeSnippetUtils {

    private static final int SURROUNDING_LINES = 2;
    private static final int ARROW_SIZE = 4;

    /**
     * Generate a snippet of code from the given code, starting 2 lines before the given line
     * and ending 2 lines after the given line (inclusive), with an arrow pointing towards the line.
     *
     * @param lines      The lines of the source code.
     * @param lineNumber The line number to point to (1-indexed).
     * @return A snippet of code with an arrow pointing to the given line.
     */
    public static String generateCodeSnippet(List<String> lines, int lineNumber) {
        final int lineNumberZeroIndexed = lineNumber - 1;

        // extract relevant lines
        int start = Math.max(0, lineNumberZeroIndexed - SURROUNDING_LINES);
        int end = Math.min(lines.size() - 1, lineNumberZeroIndexed + SURROUNDING_LINES);
        List<String> linesToShow = lines.subList(start, end + 1);

        // trim spaces at the beginning of the extracted lines
        int spacesToTrim = linesToShow.stream()
                .filter(s -> !s.isBlank())
                .mapToInt(CodeSnippetUtils::getNumberOfLeadingSpaces)
                .min()
                .orElse(0);
        String[] trimmedLines = linesToShow.stream()
                .map(x -> x.isBlank() ? "" : x.substring(spacesToTrim))
                .toArray(String[]::new);

        // add arrow or spaces
        // (prepend "--> " to the line, and "    " to all other lines)
        int relativeLineNumber = lineNumberZeroIndexed - start;
        for (int i = 0; i < trimmedLines.length; i++) {
            if (trimmedLines[i].isBlank()) continue;
            String s = i == relativeLineNumber ?
                    "-".repeat(ARROW_SIZE - 2) + "> " :
                    " ".repeat(ARROW_SIZE);
            trimmedLines[i] = s + trimmedLines[i];
        }

        return String.join("\n", trimmedLines);
    }

    private static int getNumberOfLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }
}
