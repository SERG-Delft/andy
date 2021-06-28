package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import org.pitest.mutationtest.commandline.OptionsParser;
import org.pitest.mutationtest.commandline.ParseResult;
import org.pitest.mutationtest.commandline.PluginFilter;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.Unchecked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static nl.tudelft.cse1110.grader.util.ClassUtils.allClassesButTestingOnes;
import static nl.tudelft.cse1110.grader.util.ClassUtils.getTestClass;

public class RunPitest implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        final PluginServices plugins = PluginServices.makeForContextLoader();
        final OptionsParser parser = new OptionsParser(new PluginFilter(plugins));
        final ParseResult pr = parser.parse(buildArgs(cfg));

        if (!pr.isOk()) {
            result.genericFailure(this, pr.getErrorMessage().get());
            flow.next(new GenerateResultsStep());
        } else {
            final ReportOptions data = pr.getOptions();
            final CombinedStatistics stats = runReport(data, plugins);

            result.logPitest(stats);

            flow.next(new RunJacoco());
        }
    }

    private String[] buildArgs(Configuration cfg) {

        List<String> args = new ArrayList<>();

        args.add("--reportDir");
        args.add(cfg.getReportsDir());

        args.add("--targetClasses");
        args.add(commaSeparated(allClassesButTestingOnes(cfg.getNewClassNames())));

        args.add("--targetTests");
        args.add(getTestClass(cfg.getNewClassNames()));

        args.add("--sourceDirs");
        args.add(cfg.getWorkingDir());

        args.add("--verbose");
        args.add("true");

        args.add("--classPath");
        args.add(cfg.getWorkingDir());

        System.out.println(args);
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
}
