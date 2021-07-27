package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.AdditionalReportJUnitListener;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.util.ClassUtils;

import nl.tudelft.cse1110.grader.util.FileUtils;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.jacoco.report.html.HTMLFormatter;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJacoco implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        this.backup(cfg, result);
    }

    void backup(Configuration cfg, ResultBuilder result) {
        try {
            String testClass = ClassUtils.getTestClass(cfg.getNewClassNames());
            List<String> otherClasses = ClassUtils.allClassesButTestingOnes(cfg.getNewClassNames());

            final IRuntime runtime = new LoggerRuntime();

            final Instrumenter instr = new Instrumenter(runtime);

            InstrumentClassLoader classLoader = new InstrumentClassLoader();

            InputStream originalSolution = this.getClassAsInputStream(cfg.getWorkingDir(), testClass);
            classLoader.addDefinition(testClass, originalSolution.readAllBytes());
            originalSolution.close();

            for (String libraryClass : otherClasses) {
                InputStream originalLibrary = this.getClassAsInputStream(cfg.getWorkingDir(), libraryClass);
                final byte[] instrumentedLibrary = instr.instrument(originalLibrary, libraryClass);
                originalLibrary.close();
                classLoader.addDefinition(libraryClass, instrumentedLibrary);
            }

            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);

            final RuntimeData data = new RuntimeData();
            runtime.startup(data);

            this.executeJUnitTests(testClass);

            final ExecutionDataStore executionData = new ExecutionDataStore();
            final SessionInfoStore sessionInfos = new SessionInfoStore();
            data.collect(executionData, sessionInfos, false);
            runtime.shutdown();

            final CoverageBuilder coverageBuilder = new CoverageBuilder();
            final Analyzer analyzer = new Analyzer(executionData, coverageBuilder);

            for (String libraryClass : otherClasses) {
                InputStream originalLibrary = this.getClassAsInputStream(cfg.getWorkingDir(), libraryClass);
                analyzer.analyzeClass(originalLibrary, libraryClass);
                originalLibrary.close();
            }

            Collection<IClassCoverage> coverages = coverageBuilder.getClasses();
            result.logJacoco(coverages);

            this.generateReport(cfg, testClass, coverageBuilder, executionData, sessionInfos);

            Thread.currentThread().setContextClassLoader(oldClassLoader);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    private InputStream getClassAsInputStream(String filepath, String className) throws IOException {
        String pathToClass = filepath + "/" + className.replace('.', '/') + ".class";
        return new FileInputStream(pathToClass);
    }

    private void executeJUnitTests(String testClass) {
        Launcher launcher = LauncherFactory.create();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(testClass))
                .build();
        launcher.execute(request);
    }

    private void generateReport(Configuration cfg, String testClass,
                                CoverageBuilder coverageBuilder, ExecutionDataStore executionData,
                                SessionInfoStore sessionInfos) throws IOException {
        final HTMLFormatter htmlFormatter = new HTMLFormatter();
        final IReportVisitor visitor = htmlFormatter
                .createVisitor(new FileMultiReportOutput(new File(cfg.getReportsDir())));

        visitor.visitInfo(sessionInfos.getInfos(),
                executionData.getContents());

        visitor.visitBundle(coverageBuilder.getBundle(testClass),
                new DirectorySourceFileLocator(new File(cfg.getWorkingDir()), "utf-8", 4));

        visitor.visitEnd();
    }

    private static class InstrumentClassLoader extends ClassLoader {
        private final Map<String, byte[]> definitions = new HashMap<>();

        public void addDefinition(final String name, final byte[] bytes) {
            definitions.put(name, bytes);
        }

        @Override
        protected Class<?> loadClass(final String name, final boolean resolve)
                throws ClassNotFoundException {
            final byte[] bytes = definitions.get(name);
            if (bytes != null) {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.loadClass(name, resolve);
        }
    }
}
