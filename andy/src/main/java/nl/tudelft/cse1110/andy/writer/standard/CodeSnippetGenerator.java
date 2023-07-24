package nl.tudelft.cse1110.andy.writer.standard;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.utils.CodeSnippetUtils;

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
        return this.generateCodeSnippetFromFile(solutionPath, line);
    }

    private String generateCodeSnippetFromFile(String path, int line) {
        // read file
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return CodeSnippetUtils.generateCodeSnippet(lines, line);
    }
}
