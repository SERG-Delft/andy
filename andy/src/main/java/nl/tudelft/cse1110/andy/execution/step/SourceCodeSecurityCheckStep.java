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

        if (!checkForKeywords(code, result)) return;
    }

    private boolean checkForKeywords(String code, ResultBuilder result) {
        String msg = "It is not allowed to use '%s' in your code";
        String seleniumUnsupportedDriverMsg = "Please use the provided method to instantiate a Selenium driver";
        var keywords = Map.ofEntries(
                // Block attempts to access the RunConfiguration
                Map.entry("Configuration", msg),

                // Block reflection
                Map.entry("forName", msg),
                Map.entry("getAnnotation", msg), // also blocks getAnnotations
                Map.entry("ClassLoader", msg), // blocks ClassLoader-related methods
                Map.entry("getConstructor", msg), // also blocks getConstructors
                Map.entry("getDeclaredAnnotation", msg), // also blocks getDeclaredAnnotations/-ByType
                Map.entry("getDeclaredConstructor", msg), // also blocks getDeclaredConstructors
                Map.entry("getDeclaredField", msg), // also blocks getDeclaredFields
                Map.entry("getDeclaredMethod", msg), // also blocks getDeclaredMethods
                Map.entry("getDeclaredMethods", msg),
                Map.entry("getEnclosingConstructor", msg),
                Map.entry("getEnclosingMethod", msg),
                Map.entry("getField", msg), // also blocks getFields
                Map.entry("getMethod", msg), // also blocks getMethods
                Map.entry("getModifiers", msg),
                Map.entry("newInstance", msg),
                Map.entry("reflect", msg),
                Map.entry("setAccessible", msg),
                Map.entry("currentThread", msg),
                Map.entry("getStackTrace", msg),
                Map.entry("getContextClassLoader", msg),

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
                result.compilationSecurityFail(String.format(keywords.get(keyword), keyword));
                return false;
            }
        }

        return true;
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
