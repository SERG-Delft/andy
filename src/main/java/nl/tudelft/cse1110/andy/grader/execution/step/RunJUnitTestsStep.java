package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.AdditionalReportJUnitListener;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.util.ClassUtils;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class RunJUnitTestsStep implements ExecutionStep {

    private Map<TestIdentifier, ReportEntry> additionalReports = new HashMap<>();

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        try {

            SummaryGeneratingListener listener = new SummaryGeneratingListener();
            AdditionalReportJUnitListener additionalReportJUnitListener = new AdditionalReportJUnitListener(this);

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
                result.logConsoleOutput(output.toString());

            /* Log the junit result */
            logJUnitRun(result, summary);
        } catch (Exception e) {
            result.genericFailure(this, e);
        }

    }

    private void logJUnitRun(ResultBuilder result, TestExecutionSummary summary) {
        if (summary.getTestsFoundCount() == 0) {
            noTestsFound(result, summary);
        } else {

            result.message("\n--- JUnit execution");
            result.message(String.format("%d/%d passed", summary.getTestsSucceededCount(), summary.getTestsFoundCount()));

            for (TestExecutionSummary.Failure failure : summary.getFailures()) {
                this.logJUnitFailedTest(result, failure);
            }

            result.setJUnitResults((int) summary.getTestsStartedCount(), (int) summary.getTestsSucceededCount());

            if(summary.getTestsSucceededCount() < summary.getTestsFoundCount()) {
                result.message("You must ensure that all tests are passing! Stopping the assessment.");
                result.failed();
            }
        }
    }

    private void logJUnitFailedTest(ResultBuilder result, TestExecutionSummary.Failure failure) {
        boolean isParameterizedTest = failure.getTestIdentifier().getUniqueId().contains("test-template-invocation");
        boolean isPBT = failure.getTestIdentifier().getUniqueId().contains("property");

        if(isParameterizedTest) {
            String methodName = this.getParameterizedMethodName(failure);
            String testCaseNumber = this.getParameterizedTestCaseNumber(failure);
            result.message(String.format("\n- Parameterized test \"%s\", test case #%s failed:", methodName, testCaseNumber));
            result.message(String.format("%s", failure.getException()));
        } else if (isPBT) {
            result.message(String.format("\n- Property test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));

            if (this.additionalReports.containsKey(failure.getTestIdentifier())) {
                result.message(this.additionalReports.get(failure.getTestIdentifier()).getKeyValuePairs().toString());
            }
        } else {
            result.message(String.format("\n- Test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));
            result.message(String.format("%s", simplifyTestErrorMessage(failure)));
        }
    }

    private String simplifyTestErrorMessage(TestExecutionSummary.Failure failure) {
        if (failure.getException().toString()
                .contains("Cannot invoke non-static method")) {
            String failingMethod = getFailingMethod(failure);

            return "Make sure your corresponding method " + failingMethod + " is static!";
        } else if (failure.getException().toString()
                .contains("You must configure at least one set of arguments"))    {
            return "Make sure you have provided a @MethodSource for this @ParameterizedTest!";
        }
        return failure.getException().toString();
    }

    private String getParameterizedMethodName(TestExecutionSummary.Failure failure) {
        int endIndex = failure.getTestIdentifier().getLegacyReportingName().indexOf('(');
        return failure.getTestIdentifier().getLegacyReportingName().substring(0, endIndex);
    }


    private String getParameterizedTestCaseNumber(TestExecutionSummary.Failure failure) {
        int open = failure.getTestIdentifier().getLegacyReportingName().lastIndexOf('[');
        int close = failure.getTestIdentifier().getLegacyReportingName().lastIndexOf(']');

        return failure.getTestIdentifier().getLegacyReportingName().substring(open+1, close);
    }

    private String getFailingMethod(TestExecutionSummary.Failure failure) {
        int open = failure.getException().toString().indexOf('>');
        int close = failure.getException().toString().indexOf(']');

        return failure.getException().toString().substring(open+2, close);
    }

    /** Checks for different error cases possible when tests are not detected
     * @param summary - JUnit execution summary
     */
    private void noTestsFound(ResultBuilder result, TestExecutionSummary summary) {

        if (summary.getContainersFoundCount() > summary.getContainersStartedCount()) {
            result.message("--- Warning\nWe do not see any tests.\n" +
                    "Please check for the following JUnit pre-conditions:\n" +
                    "- @BeforeAll and @AfterAll methods should be static\n" +
                    "- @BeforeEach methods should be non-static\n");
        } else {
            result.message("--- Warning\nWe do not see any tests.\n" +
                    "Please check for the following JUnit pre-conditions:\n" +
                    "- Normal tests must be annotated with \"@Test\"\n" +
                    "- Parameterized tests must be annotated with \"@ParameterizedTest\"\n" +
                    "- Method sources must be static and provided as: \"@MethodSource(\"generator\")\" e.g.\n" +
                    "- Property based tests must be annotated with \"@Property\"\n");
        }
        result.failed();
    }

    public void logAdditionalReport(TestIdentifier testIdentifier, ReportEntry report) {
        this.additionalReports.put(testIdentifier, report);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunJUnitTestsStep;
    }
}


