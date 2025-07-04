package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.utils.ClassUtils;
import nl.tudelft.cse1110.andy.utils.FilesUtils;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTestsStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            AdditionalReportJUnitListener additionalReportJUnitListener = new AdditionalReportJUnitListener(result);

            String testClass = ClassUtils.getTestClass(ctx.getNewClassNames());
            Class<?> clazz = Class.forName(testClass, false, Thread.currentThread().getContextClassLoader());

            /* Change the sysout so that we can show it to the student later */
            PrintStream console = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Launcher launcher = LauncherFactory.create();
            launcher.registerTestExecutionListeners(listener);
            launcher.registerTestExecutionListeners(additionalReportJUnitListener);

            /* Set jqwik configuration options */
            Properties jqwikProperties = new Properties();
            jqwikProperties.setProperty("jqwik.tries.default", "500");
            jqwikProperties.setProperty("jqwik.shrinking.default", "OFF");
            jqwikProperties.setProperty("jqwik.maxdiscardratio.default", "2");
            // This file is also used during Pitest execution
            Path jqwikPropertiesFilePath = Paths.get(ctx.getDirectoryConfiguration().getWorkingDir(), "junit-platform.properties");
            jqwikProperties.store(Files.newOutputStream(jqwikPropertiesFilePath), null);

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(clazz))
                    .configurationParameter("jqwik.reporting.usejunitplatform", "true")
                    // Enable shrinking for non-Pitest test execution (this takes precedence over the properties file)
                    .configurationParameter("jqwik.shrinking.default", "BOUNDED")
                    .configurationParameter("jqwik.database", FilesUtils.createTemporaryDirectory("jqwik").resolve("jqwik-db").toString())
                    .build();
            launcher.execute(request);

            TestExecutionSummary summary = listener.getSummary();

            /* Restore the sysout back, and put it in the result in case there's something */
            System.setOut(console);

            /* Log the junit result */
            result.logJUnitRun(summary, output.toString());
        } catch (Exception e) {
            result.genericFailure(this, e);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunJUnitTestsStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static class AdditionalReportJUnitListener implements TestExecutionListener {

        private ResultBuilder result;

        public AdditionalReportJUnitListener(ResultBuilder result) {
            this.result = result;
        }

        @Override
        public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
            this.result.logAdditionalReport(testIdentifier, entry);
        }
    }
}


