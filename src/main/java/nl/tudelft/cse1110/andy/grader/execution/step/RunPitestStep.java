package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.grader.config.RunConfiguration;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.util.ClassUtils;
import nl.tudelft.cse1110.andy.grader.util.FilesUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

            logResults(ctx, result, stats);
        }
    }

    private void logResults(Context ctx, ResultBuilder result, CombinedStatistics stats) {

        int detectedMutations = (int)(stats.getMutationStatistics().getTotalDetectedMutations());

        /*
         * If number of mutations is -1, we use the one provided by Pitest.
         * If it's greater than 0, we use the one provided and ignore Pitest's one.
         */
        int numberOfMutationsToConsider = ctx.getRunConfiguration().numberOfMutationsToConsider();
        boolean numberOfMutantsToConsiderIsOverridden = numberOfMutationsToConsider != -1;

        int totalMutations = numberOfMutantsToConsiderIsOverridden ?
                numberOfMutationsToConsider :
                (int) stats.getMutationStatistics().getTotalMutations();

        result.message("\n--- Mutation testing");
        result.message(String.format("%d/%d killed", detectedMutations, totalMutations));

        result.setMutationGrade(detectedMutations, totalMutations);

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
        args.add(dirCfg.getWorkingDir());

        args.add("--mutators");
        args.add(commaSeparated(runCfg.listOfMutants()));

        return args.stream().toArray(String[]::new);
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
