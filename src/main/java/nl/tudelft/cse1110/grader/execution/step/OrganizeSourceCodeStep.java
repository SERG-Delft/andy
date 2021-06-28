package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static nl.tudelft.cse1110.grader.util.ClassUtils.extractPackageName;
import static nl.tudelft.cse1110.grader.util.ClassUtils.packageToDirectory;
import static nl.tudelft.cse1110.grader.util.FileUtils.*;

public class OrganizeSourceCodeStep implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        try {
            List<String> listOfFiles = filePathsAsString(getAllJavaFiles(cfg.getWorkingDir()));

            for(String pathOfJavaClass : listOfFiles) {
                String content = new String(Files.readAllBytes(Paths.get(pathOfJavaClass)));

                String packageName = extractPackageName(content);
                String directoryName = concatenateDirectories(cfg.getWorkingDir(), packageToDirectory(packageName));

                createDirIfNeeded(directoryName);
                moveFile(pathOfJavaClass, directoryName, new File(pathOfJavaClass).getName());
            }

            flow.next(new CompilationStep());
        } catch (Exception e) {
            result.genericFailure(this, e);
            flow.next(new GenerateResultsStep());
        }
    }


}
