package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.execution.step.RunJUnitTestsStep;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class AdditionalReportJUnitListener implements TestExecutionListener {

    private final RunJUnitTestsStep step;

    public AdditionalReportJUnitListener(RunJUnitTestsStep step) {
        this.step = step;
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        this.step.logAdditionalReport(testIdentifier, entry);
    }
}
