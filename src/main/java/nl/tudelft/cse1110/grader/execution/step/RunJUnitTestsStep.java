package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.config.DirectoryConfiguration;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.execution.step.helper.AdditionalReportJUnitListener;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static nl.tudelft.cse1110.grader.util.ClassUtils.getTestClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTestsStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        DirectoryConfiguration dirCfg = cfg.getDirectoryConfiguration();

        try {

            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            AdditionalReportJUnitListener additionalReportJUnitListener = new AdditionalReportJUnitListener(result);

            String testClass = getTestClass(dirCfg.getNewClassNames());

            PrintStream console = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Launcher launcher = LauncherFactory.create();
            launcher.registerTestExecutionListeners(listener);
            launcher.registerTestExecutionListeners(additionalReportJUnitListener);

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(testClass))
                    .configurationParameter("jqwik.reporting.usejunitplatform", "true")
                    .build();
            launcher.execute(request);

            TestExecutionSummary summary = listener.getSummary();

            System.setOut(console);

            if(output.size() > 0)
                result.logConsoleOutput(output);
            result.logJUnitRun(summary);
        } catch (Exception e) {
            result.genericFailure(this, e);
        }

    }
}


