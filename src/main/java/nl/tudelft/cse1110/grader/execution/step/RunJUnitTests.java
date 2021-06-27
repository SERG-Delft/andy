package nl.tudelft.cse1110.grader.execution.step;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.ExecutionFlow;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.result.ResultBuilder;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;

import static nl.tudelft.cse1110.grader.util.ClassUtils.getTestClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTests implements ExecutionStep {
    @Override
    public void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result) {
        try {
            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            Class<?> testClass = getTestClass(cfg.getNewClassNames());

            Launcher launcher = LauncherFactory.create();
            launcher.registerTestExecutionListeners(listener);

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(testClass))
                    .build();
            launcher.execute(request);

            TestExecutionSummary summary = listener.getSummary();
            summary.printTo(new PrintWriter(System.out));

            flow.next(new GenerateResultsStep());
        } catch (Exception e) {
            result.genericFailure(this, e);
            flow.next(new GenerateResultsStep());
        }

    }
}
