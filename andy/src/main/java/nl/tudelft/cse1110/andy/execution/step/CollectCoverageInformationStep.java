package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.utils.FromBytesClassLoader;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.clazzNameAsPath;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;

public class CollectCoverageInformationStep implements ExecutionStep {

    @SuppressWarnings("checkstyle:MethodLength") // in this case, it makes no sense to break down the method any farther
    @Override
    public void execute(Context ctx, ResultBuilder result) {
        // Skip step if disabled
        if (ctx.getRunConfiguration().skipJacoco()) {
            return;
        }
        if(!ctx.hasJacocoRuntime()) {
            throw new RuntimeException("Failed when getting coverage information!");
        }

        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();
        RuntimeData data = ctx.getJacocoData();
        IRuntime runtime = ctx.getJacocoRuntime();

        try {
            /*
             * After instrumenting and running, we can continue to reporting.
             * This will generate the JaCoCo report.
             */
            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            final CoverageBuilder coverageBuilder = new CoverageBuilder();
            final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);

            /*
             * We analyze the library files that have been specified in the config.
             * These classes will have a coverage report.
             */
            for (String libraryClass : runCfg.classesUnderTest()) {
                InputStream originalLibrary = this.getClassAsInputStream(dirCfg.getWorkingDir(), libraryClass);
                analyzer.analyzeClass(originalLibrary, libraryClass);
                originalLibrary.close();
            }

            Collection<IClassCoverage> coverages = coverageBuilder.getClasses();
            result.logJacoco(coverages);

            /* Generate an HTML report.*/
            String testClass = ClassUtils.getTestClass(ctx.getNewClassNames());
            this.generateReport(dirCfg, testClass, coverageBuilder, executionData, sessionInfos);

            /*
            Log the lines covered by each test by scanning the method line by line.
             */
            Map<String, String> tests = result.getQualityResult().getUnitTests();

            Map<String, Set<Integer>> coveragePerTest = linesCoveredPerTest(ctx, testClass, tests);

            result.logCoveragePerTest(coveragePerTest);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            /* Restore the old class loader to get the non-instrumented classes back.*/
            Thread.currentThread().setContextClassLoader(ctx.getClassloaderWithStudentsCode());
        }
    }

    private Map<String, Set<Integer>> linesCoveredPerTest(Context ctx, String testClass, Map<String, String> tests) throws Exception {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();
        Map<String, Set<Integer>> coveragePerTest = new HashMap<>();

        for (Map.Entry<String, String> entry : tests.entrySet()) {
            String uniqueId = entry.getKey();
            String displayName = entry.getValue();

            // 1. Fresh runtime + data for this test
            IRuntime runtime = new LoggerRuntime();
            RuntimeData data = new RuntimeData();
            runtime.startup(data);

            // 2. Re-instrument into a fresh classloader
            Instrumenter instr = new Instrumenter(runtime);
            ClassLoader current = Thread.currentThread().getContextClassLoader().getParent(); // use parent to avoid already-instrumented classes
            FromBytesClassLoader freshLoader = new FromBytesClassLoader(current);
            instrumentAllInDirectory(instr, new File(dirCfg.getWorkingDir()), freshLoader, "");

            // 3. Swap in the fresh classloader and run just this one test
            Thread.currentThread().setContextClassLoader(freshLoader);
            runSingleTest(uniqueId);

            // 4. Collect coverage
            ExecutionDataStore executionData = new ExecutionDataStore();
            SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            // 5. Analyze
            CoverageBuilder coverageBuilder = new CoverageBuilder();
            Analyzer analyzer = new Analyzer(executionData, coverageBuilder);
            for (String classUnderTest : runCfg.classesUnderTest()) {
                try (InputStream in = getClassAsInputStream(dirCfg.getWorkingDir(), classUnderTest)) {
                    analyzer.analyzeClass(in, classUnderTest);
                }
            }

            Set<Integer> coveredLines = extractCoveredLines(coverageBuilder, testClass);

            coveragePerTest.put(displayName, coveredLines);
        }

        // Restore original instrumented classloader for the rest of the pipeline
        Thread.currentThread().setContextClassLoader(ctx.getClassloaderWithStudentsCode());
        return coveragePerTest;
    }

    private void runSingleTest(String testId) {
        LauncherDiscoveryRequest request =
                LauncherDiscoveryRequestBuilder.request()
                        .selectors(DiscoverySelectors.selectUniqueId(testId))
                        .build();

        Launcher launcher = LauncherFactory.create();
        launcher.execute(request);
    }

    private Set<Integer> extractCoveredLines(
            CoverageBuilder coverageBuilder,
            String testClass) {

        Set<Integer> result = new HashSet<>();

        for (IClassCoverage cc : coverageBuilder.getClasses()) {
            String className = cc.getName().replace('/', '.').concat("Tests");

            if (!className.equals(testClass)) {
                continue;
            }

            for (int line = cc.getFirstLine(); line <= cc.getLastLine(); line++) {
                ILine l = cc.getLine(line);
                if (l.getStatus() == ICounter.FULLY_COVERED ||
                        l.getStatus() == ICounter.PARTLY_COVERED) {

                    result.add(line);
                }
            }
        }
        return result;
    }

    /**Instrument all classes in a directory.*/
    private void instrumentAllInDirectory(Instrumenter instr, File directory, FromBytesClassLoader classLoader, String start) throws IOException {
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                InputStream originalLibrary = new FileInputStream(file.getAbsolutePath());
                String className = start + "." + file.getName().replace(".class", "").replace('/', '.');

                final byte[] instrumentedLibrary = instr.instrument(originalLibrary, className);
                originalLibrary.close();
                classLoader.addDefinition(className, instrumentedLibrary);
            } else if (file.isDirectory()) {
                if (!start.equals("")) {
                    instrumentAllInDirectory(instr, file, classLoader, start + "." + file.getName());
                } else {
                    instrumentAllInDirectory(instr, file, classLoader, file.getName());
                }
            }
        }
    }

    /* Get a compiled Java class as an InputStream. */
    private InputStream getClassAsInputStream(String filepath, String className) throws IOException {
        String nameOfTheClassFile = clazzNameAsPath(className);
        String pathToClass = concatenateDirectories(filepath, nameOfTheClassFile);
        return new FileInputStream(pathToClass);
    }

    /* Generate an HTML report from JaCoCo based on the coverage analysis of our classes. */
    private void generateReport(DirectoryConfiguration dirCfg, String testClass,
                                CoverageBuilder coverageBuilder, ExecutionDataStore executionData,
                                SessionInfoStore sessionInfos) throws IOException {
        final HTMLFormatter htmlFormatter = new HTMLFormatter();

        String outputJacocoDir = concatenateDirectories(dirCfg.getOutputDir(), "jacoco");
        FilesUtils.createDirIfNeeded(outputJacocoDir);

        final IReportVisitor visitor = htmlFormatter
                .createVisitor(new FileMultiReportOutput(new File(outputJacocoDir)));

        visitor.visitInfo(sessionInfos.getInfos(),
                executionData.getContents());

        visitor.visitBundle(coverageBuilder.getBundle(testClass),
                new DirectorySourceFileLocator(new File(dirCfg.getWorkingDir()), "utf-8", 4));

        visitor.visitEnd();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CollectCoverageInformationStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
