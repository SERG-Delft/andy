package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.pitest.mutationtest.commandline.OptionsParser;
import org.pitest.mutationtest.commandline.ParseResult;
import org.pitest.mutationtest.commandline.PluginFilter;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.Unchecked;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.asClassPath;

public class RunPitestStep implements ExecutionStep {


    @Override
    public void execute(Context ctx, ResultBuilder result) {
        final PluginServices plugins = PluginServices.makeForContextLoader();
        final OptionsParser parser = new OptionsParser(new PluginFilter(plugins));

        String outputPitestDir = createDirectoryForPitest(ctx);
        final ParseResult pr = parser.parse(buildArgs(ctx, outputPitestDir));

        if (!pr.isOk()) {
            result.genericFailure("PITEST: " + pr.getErrorMessage().get());
        } else {
            final ReportOptions data = pr.getOptions();
            final CombinedStatistics stats = runReport(data, plugins);

            extractAndRemoveReportFolder(outputPitestDir);

            result.logPitest(stats);
        }
    }

    private String createDirectoryForPitest(Context ctx) {
        String outputPitestDir = FilesUtils.concatenateDirectories(ctx.getDirectoryConfiguration().getOutputDir(), "pitest");
        FilesUtils.createDirIfNeeded(outputPitestDir);
        return outputPitestDir;
    }

    private String[] buildArgs(Context ctx, String pitestOutputDir) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        List<String> args = new ArrayList<>();

        args.add("--reportDir");
        args.add(pitestOutputDir);

        args.add("--targetClasses");
        args.add(commaSeparated(runCfg.classesUnderTest()));

        args.add("--targetTests");
        args.add(ClassUtils.getTestClass(ctx.getNewClassNames()));

        args.add("--sourceDirs");
        args.add(dirCfg.getWorkingDir());

        args.add("--verbose");
        args.add("false");

        args.add("--classPath");
        String classPath = createClassPath(ctx, dirCfg);
        args.add(classPath);

        args.add("--mutators");
        args.add(commaSeparated(runCfg.listOfMutants()));

        return args.stream().toArray(String[]::new);
    }

    private String createClassPath(Context ctx, DirectoryConfiguration dirCfg) {
        List<String> toAddToClassPath = new ArrayList<>();
        toAddToClassPath.add(dirCfg.getWorkingDir());
        if(ctx.hasLibrariesToBeIncluded()) {
            toAddToClassPath.addAll(ctx.getLibrariesToBeIncluded());
        }
        return asClassPath(toAddToClassPath);
    }

    private String commaSeparated(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    private static CombinedStatistics runReport(ReportOptions data,
                                                PluginServices plugins) {

        final EntryPoint e = new EntryPoint();
        final AnalysisResult result = e.execute(null, data, plugins,
                new HashMap<>());
        if (result.getError().isPresent()) {
            throw Unchecked.translateCheckedException(result.getError().get());
        }
        return result.getStatistics().get();

    }

    private void extractAndRemoveReportFolder(String outputPitestDir) {
        /*
         * Pitest creates a subdir with the timestamp, which we want to remove.
         * The report folder "year-month-day-time" will be the only file in .../output/pitest,
         * as every WebLab submission gets a "clean" image.
         */
        try {
            File[] contentsOfPitestOutputDir = FilesUtils.getAllFiles(new File(outputPitestDir));

            if (contentsOfPitestOutputDir.length != 0) {
                File reportFolderToSkip = contentsOfPitestOutputDir[0];

                FileUtils.copyDirectory(reportFolderToSkip, new File(outputPitestDir),true);
                FilesUtils.deleteDirectory(reportFolderToSkip);
            } else {
                throw new RuntimeException("PiTest report was not written to .../output/pitest!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunPitestStep;
    }
}
