package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
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

        if (!checkPackageName(code, result)) return;

        if (!checkForKeywords(code, result)) return;
    }

    private boolean checkPackageName(String code, ResultBuilder result) {
        Pattern pattern = Pattern.compile("^\\s*package\\s+delft\\s*;.*", Pattern.DOTALL);
        if (!pattern.matcher(code).find()) {
            result.compilationSecurityFail("The package name of your solution must be \"delft\"");
            return false;
        }

        return true;
    }

    private boolean checkForKeywords(String code, ResultBuilder result) {
        String reflectionMsg = "Using reflection in your code is not allowed";
        String seleniumUnsupportedDriverMsg = "Please use HtmlUnitDriver as WebLab does not have a browser installed";
        // An immutable map only supports 10 hardcoded KVs, we need more, hence HashMap wrapper.
        var keywords = new java.util.HashMap<>(Map.of(
                "Configuration", "Accessing the task configuration in your code is not allowed",
                "forName", reflectionMsg,
                "getDeclaredConstructor", reflectionMsg,
                "getDeclaredMethods", reflectionMsg,
                "getField", reflectionMsg,
                "getModifiers", reflectionMsg,
                "reflect", reflectionMsg,
                "setAccessible", reflectionMsg
        ));
        keywords.put("ChromeDriver", seleniumUnsupportedDriverMsg);
        keywords.put("ChromiumDriver", seleniumUnsupportedDriverMsg);
        keywords.put("EdgeDriver", seleniumUnsupportedDriverMsg);
        keywords.put("FirefoxDriver", seleniumUnsupportedDriverMsg);
        keywords.put("InternetExplorerDriver", seleniumUnsupportedDriverMsg);
        keywords.put("OperaDriver", seleniumUnsupportedDriverMsg);
        keywords.put("SafariDriver", seleniumUnsupportedDriverMsg);
        for (String keyword : keywords.keySet()) {
            if (code.contains(keyword)) {
                result.compilationSecurityFail(keywords.get(keyword));
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof SourceCodeSecurityCheckStep;
    }
}
