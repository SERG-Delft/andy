package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.SecureExamRunConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FilesUtils;

import java.io.File;
import java.util.Collection;

public class ExamModeSecurityGuardStep implements ExecutionStep {
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        boolean isExam = ctx.getRunConfiguration().mode() == Mode.EXAM;
        if(!isExam) {
            return;
        }

        // replace the configuration with a default one
        RunConfiguration defaultConfig = new SecureExamRunConfiguration(ctx.getRunConfiguration());
        ctx.setRunConfiguration(defaultConfig);

        // remove the configuration source code and byte code from the disk
        Collection<File> javaFiles = FilesUtils.getAllFiles(ctx.getDirectoryConfiguration().getWorkingDir(), "java");
        Collection<File> byteCode = FilesUtils.getAllFiles(ctx.getDirectoryConfiguration().getWorkingDir(), "class");

        deleteConfigurationFrom(javaFiles);
        deleteConfigurationFrom(byteCode);

        // we replace the classloader again as the current Configuration has already been
        // loaded by the current classloader.
        ctx.getFlow().addStepAsNext(new ReplaceClassloaderStep());

    }

    private void deleteConfigurationFrom(Collection<File> files) {
        files.stream()
                .filter(f -> f.getName().contains("Configuration"))
                .forEach(f -> FilesUtils.deleteFile(f));

    }
}
