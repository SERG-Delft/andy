package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SourceCodeSecurityCheckStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        List<File> solutionFile = FilesUtils.getAllJavaFiles(dirCfg.getWorkingDir()).stream()
                .filter(f -> f.getName().equals("Solution.java"))
                .collect(Collectors.toList());
        if (solutionFile.size() != 1) {
            // Could not find solution file
            return;
        }

        String code;
        try {
            code = Files.readString(Path.of(solutionFile.get(0).getPath()));
        } catch (IOException e) {
            result.genericFailure(this, e);
            return;
        }

        code = removeComments(code);
        if (!checkPackageName(code, result)) return;

        if (!checkForKeywords(code, result)) return;
    }

    private boolean checkPackageName(String code, ResultBuilder result) {
        Pattern pattern = Pattern.compile("^\\s*package\\s+delft\\s*;.*", Pattern.DOTALL);
        if (!pattern.matcher(code).find()) {
            result.compilationSecurityFail("The package name of your solution must be \"delft\"", null);
            return false;
        }

        return true;
    }

    private boolean checkForKeywords(String code, ResultBuilder result) {
        String reflectionMsg = "Using reflection in your code is not allowed";
        String seleniumUnsupportedDriverMsg = "Please use HtmlUnitDriver as Andy does not have a browser installed";
        var keywords = Map.ofEntries(
                // Block attempts to access the RunConfiguration
                Map.entry("Configuration", "Accessing the task configuration in your code is not allowed"),

                // Block reflection
                Map.entry("forName", reflectionMsg),
                Map.entry("getAnnotation", reflectionMsg), // also blocks getAnnotations
                Map.entry("ClassLoader", reflectionMsg), // blocks ClassLoader-related methods
                Map.entry("getConstructor", reflectionMsg), // also blocks getConstructors
                Map.entry("getDeclaredAnnotation", reflectionMsg), // also blocks getDeclaredAnnotations/-ByType
                Map.entry("getDeclaredConstructor", reflectionMsg), // also blocks getDeclaredConstructors
                Map.entry("getDeclaredField", reflectionMsg), // also blocks getDeclaredFields
                Map.entry("getDeclaredMethod", reflectionMsg), // also blocks getDeclaredMethods
                Map.entry("getDeclaredMethods", reflectionMsg),
                Map.entry("getEnclosingConstructor", reflectionMsg),
                Map.entry("getEnclosingMethod", reflectionMsg),
                Map.entry("getField", reflectionMsg), // also blocks getFields
                Map.entry("getMethod", reflectionMsg), // also blocks getMethods
                Map.entry("getModifiers", reflectionMsg),
                Map.entry("newInstance", reflectionMsg),
                Map.entry("reflect", reflectionMsg),
                Map.entry("setAccessible", reflectionMsg),

                // Block unsupported Selenium drivers
                Map.entry("ChromeDriver", seleniumUnsupportedDriverMsg),
                Map.entry("ChromiumDriver", seleniumUnsupportedDriverMsg),
                Map.entry("EdgeDriver", seleniumUnsupportedDriverMsg),
                Map.entry("FirefoxDriver", seleniumUnsupportedDriverMsg),
                Map.entry("InternetExplorerDriver", seleniumUnsupportedDriverMsg),
                Map.entry("OperaDriver", seleniumUnsupportedDriverMsg),
                Map.entry("SafariDriver", seleniumUnsupportedDriverMsg)
        );
        for (String keyword : keywords.keySet()) {
            if (code.contains(keyword)) {
                String[] lines = code.split("\\n");
                // Split lines and search every line for keyword
                for (int lineNumber = 0; lineNumber < lines.length; lineNumber++)
                    if (lines[lineNumber].contains(keyword)) {
                        // Adding 1 to lineNumber to convert from zero-based index to one-based index
                        result.compilationSecurityFail(keywords.get(keyword), Optional.of(lineNumber+1));
                        return false;
                    }
            }
        }

        return true;
    }

    public String removeComments(String code) {
        // Remove single-line comments (//)
        code = code.replaceAll("//.*", "");

        // Remove multi-line comments (/* */)
        Pattern pattern = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(code);
        code = matcher.replaceAll("");

        return code;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SourceCodeSecurityCheckStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
