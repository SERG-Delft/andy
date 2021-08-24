package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.AdditionalReportJUnitListener;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.util.ClassUtils;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTestsStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {

            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            AdditionalReportJUnitListener additionalReportJUnitListener = new AdditionalReportJUnitListener(result);

            String testClass = ClassUtils.getTestClass(ctx.getNewClassNames());
            Class<?> clazz = Class.forName(testClass, false, Thread.currentThread().getContextClassLoader());
            result.debug(this, String.format("Name of the test class: %s", clazz.getName()));

            /* Change the sysout so that we can show it to the student later */
            PrintStream console = System.out;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            Launcher launcher = LauncherFactory.create();
            launcher.registerTestExecutionListeners(listener);
            launcher.registerTestExecutionListeners(additionalReportJUnitListener);

            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(selectClass(clazz))
                    .configurationParameter("jqwik.reporting.usejunitplatform", "true")
                    .build();
            launcher.execute(request);

            TestExecutionSummary summary = listener.getSummary();
            result.debug(this, String.format("JUnit ran %d tests", summary.getTestsFoundCount()));

            /* Restore the sysout back, and put it in the result in case there's something */
            System.setOut(console);
            if(output.size() > 0)
                result.logConsoleOutput(output);

            /* Log the junit result */
            result.logJUnitRun(summary);
        } catch (Exception e) {
            result.genericFailure(this, e);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunJUnitTestsStep;
    }
}


