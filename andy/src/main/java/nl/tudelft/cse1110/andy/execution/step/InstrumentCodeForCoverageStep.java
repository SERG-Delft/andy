package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.FromBytesClassLoader;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class InstrumentCodeForCoverageStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {

        // Skip step if disabled
        if (ctx.getRunConfiguration().skipJacoco()) {
            return;
        }

        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();

        try {
            IRuntime runtime = new LoggerRuntime();

            /*
             * We need to instrument the SUT to be able to generate a JaCoCo report.
             * We then override the non-instrumented classes with the instrumented ones using
             * a custom class loader. For the sake of simplicity we instrument all classes using a custom method.
             * We also save the old class loader to restore it later.
             */
            final Instrumenter instr = new Instrumenter(runtime);

            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            FromBytesClassLoader classLoaderWithInstrumentedClasses = new FromBytesClassLoader(currentClassLoader);

            this.instrumentAllInDirectory(instr, new File(dirCfg.getWorkingDir()), classLoaderWithInstrumentedClasses, "");

            Thread.currentThread().setContextClassLoader(classLoaderWithInstrumentedClasses);

            final RuntimeData data = new RuntimeData();
            runtime.startup(data);

            ctx.setJacocoObjects(runtime, data);

        } catch (Exception ex) {
            result.genericFailure(this, ex);
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

    @Override
    public boolean equals(Object other) {
        return other instanceof InstrumentCodeForCoverageStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
