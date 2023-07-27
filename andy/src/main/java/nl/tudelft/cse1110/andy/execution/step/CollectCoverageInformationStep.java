package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import nl.tudelft.cse1110.andy.utils.FromBytesClassLoader;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.report.DirectorySourceFileLocator;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static nl.tudelft.cse1110.andy.utils.ClassUtils.clazzNameAsPath;
import static nl.tudelft.cse1110.andy.utils.FilesUtils.concatenateDirectories;

public class CollectCoverageInformationStep implements ExecutionStep {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            /* Restore the old class loader to get the non-instrumented classes back.*/
            Thread.currentThread().setContextClassLoader(ctx.getClassloaderWithStudentsCode());
        }
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
