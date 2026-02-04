package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.htmlunit.cyberneko.xerces.dom.ElementImpl;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.mutationtest.MutationResult;
import org.pitest.mutationtest.MutationStatusTestPair;
import org.pitest.mutationtest.commandline.OptionsParser;
import org.pitest.mutationtest.commandline.ParseResult;
import org.pitest.mutationtest.commandline.PluginFilter;
import org.pitest.mutationtest.config.PluginServices;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.tooling.AnalysisResult;
import org.pitest.mutationtest.tooling.CombinedStatistics;
import org.pitest.mutationtest.tooling.EntryPoint;
import org.pitest.util.Unchecked;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class RunPitestStep implements ExecutionStep {


    @Override
    public void execute(Context ctx, ResultBuilder result) {

        // Skip step if disabled
        if (ctx.getRunConfiguration().skipPitest()) {
            return;
        }

        final PluginServices plugins = PluginServices.makeForContextLoader();
        final OptionsParser parser = new OptionsParser(new PluginFilter(plugins));

        String outputPitestDir = createDirectoryForPitest(ctx);
        final ParseResult pr = parser.parse(buildArgs(ctx, outputPitestDir));

        if (!pr.isOk()) {
            result.genericFailure("PITEST: " + pr.getErrorMessage().get());
        } else {
            final ReportOptions data = pr.getOptions();

            // let's silence the sysout and java logging
            PrintStream console = silencePitest();

            // run!
            final CombinedStatistics stats = runReport(ctx, data, plugins);

            System.setOut(console);

            result.logPitest(stats);
        }

        /*
        Log the mutations killed by each test.
         */
        List<String> tests = result.getQualityResult().getUnitTests();

        Map<String, Set<Integer>> mutationsKilledPerTest = mutationsKilledPerTest(ctx, tests);

        result.logMutationsKilledPerTest(mutationsKilledPerTest);
    }

    private Map<String, Set<Integer>> mutationsKilledPerTest(
            Context ctx,
            List<String> tests) {

        Map<String, Set<Integer>> result = new HashMap<>();
        for (String test : tests) result.put(test, new HashSet<>());

        Path mutationsFile = Paths.get(
                ctx.getDirectoryConfiguration().getWorkingDir(),
                "pitest",
                "mutations.xml"
        );

        if (!Files.exists(mutationsFile)) return result;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(mutationsFile.toFile());

            NodeList mutationNodes = doc.getElementsByTagName("mutation");

            for (int i = 0; i < mutationNodes.getLength(); i++) {
                ElementImpl mutation = (ElementImpl) mutationNodes.item(i);

                String status = mutation.getAttribute("status");
                if (!"KILLED".equals(status)) {
                    continue;
                }

                int mutationId = Integer.parseInt(
                        mutation.getElementsByTagName("index")
                                .item(0)
                                .getTextContent()
                );

                NodeList killingTests = mutation.getElementsByTagName("killingTest");

                if (killingTests.getLength() == 0) continue;

                String rawTestName = killingTests.item(0).getTextContent();

                String testName = normalizeTestName(rawTestName);

                result.computeIfAbsent(testName, t -> new HashSet<>()).add(mutationId);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse mutations.xml", e);
        }

        return result;
    }

    private String normalizeTestName(String pitTestName) {
        int paren = pitTestName.indexOf('(');
        if (paren != -1) {
            pitTestName = pitTestName.substring(0, paren);
        }
        return pitTestName.replace('.', '#');
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
        args.add("true"); // to allow detecting which mutations are covered by each test

        args.add("--classPath");
        List<String> librariesToInclude = compiledClassesPlusLibraries(ctx, dirCfg);
        args.add(commaSeparated(librariesToInclude));

        args.add("--mutators");
        args.add(commaSeparated(runCfg.listOfMutants()));

        args.add("--exportLineCoverage");

        return args.stream().toArray(String[]::new);
    }

    private List<String> compiledClassesPlusLibraries(Context ctx, DirectoryConfiguration dirCfg) {
        List<String> toAddToClassPath = new ArrayList<>();
        toAddToClassPath.add(dirCfg.getWorkingDir());

        if(ctx.hasLibrariesToBeIncluded()) {
            toAddToClassPath.addAll(ctx.getLibrariesToBeIncludedInCompilation());
        }

        return toAddToClassPath.stream()
                .filter(cp -> !cp.contains("pitest"))
                .toList();
    }

    private String commaSeparated(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    private static CombinedStatistics runReport(Context ctx, ReportOptions data,
                                                PluginServices plugins) {

        final EntryPoint e = new EntryPoint();
        File baseDir = new File(ctx.getDirectoryConfiguration().getWorkingDir());
        final AnalysisResult result = e.execute(baseDir, data, plugins, new HashMap<>());
        if (result.getError().isPresent()) {
            throw Unchecked.translateCheckedException(result.getError().get());
        }
        return result.getStatistics().get();

    }

    private PrintStream silencePitest() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        Logger pit = Logger.getLogger("PIT");
        Arrays.stream(pit.getHandlers()).forEach(h -> pit.removeHandler(h));
        return console;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunPitestStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
