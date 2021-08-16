package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.util.ClassUtils;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.FileUtils;
import org.pitest.mutationtest.commandline.OptionsParser;
import org.pitest.mutationtest.commandline.ParseResult;
import org.pitest.mutationtest.commandline.PluginFilter;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.Unchecked;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RunPitest implements ExecutionStep {


    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        final PluginServices plugins = PluginServices.makeForContextLoader();
        final OptionsParser parser = new OptionsParser(new PluginFilter(plugins));
        final ParseResult pr = parser.parse(buildArgs(cfg));

        if (!pr.isOk()) {
            result.genericFailure(this, pr.getErrorMessage().get());
        } else {
            final ReportOptions data = pr.getOptions();
            final CombinedStatistics stats = runReport(data, plugins);

            result.logPitest(stats);
        }
    }

    private String[] buildArgs(Configuration cfg) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();
        RunConfiguration runCfg = cfg.getRunConfiguration();

        List<String> args = new ArrayList<>();

        args.add("--reportDir");
        // Create new directory ".../output/pitest"
        // Here, the PiTest report folder in format "year-month-day-time" will be written
        String outputPitestDir = FileUtils.concatenateDirectories(dirCfg.getOutputDir(), "pitest");
        FileUtils.createDirIfNeeded(outputPitestDir);
        args.add(outputPitestDir);
        // We "skip" this folder and instead keep its contents
        extractAndRemoveReportFolder(outputPitestDir);

        args.add("--targetClasses");
        args.add(commaSeparated(runCfg.classesUnderTest()));

        args.add("--targetTests");
        args.add(ClassUtils.getTestClass(dirCfg.getNewClassNames()));

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

    /**
     * @param outputPitestDir - .../output/pitest, which will contain a folder "202108151457" e.g.
     *                        This folder will be skipped,
     *                        and instead its contents will be directly in .../output/pitest
     */
    private void extractAndRemoveReportFolder(String outputPitestDir) {

        try {
            File[] contentsOfPitestOutputDir = FileUtils.getAllFiles(new File(outputPitestDir));

            if (contentsOfPitestOutputDir.length != 0) {

                // The report folder "year-month-day-time" will be the only file in .../output/pitest,
                // as every WebLab submission gets a “clean” image.
                File reportFolderToSkip = contentsOfPitestOutputDir[0];

                File[] pitestReportFiles = FileUtils.getAllFiles(new File(reportFolderToSkip.getAbsolutePath()));
                for (File f : pitestReportFiles) {
                    FileUtils.copyFile(f.getAbsolutePath(), outputPitestDir);
                }
                FileUtils.deleteDirectory(reportFolderToSkip);
            } else {
//                throw new RuntimeException("PiTest report was not written to .../output/pitest!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

}
