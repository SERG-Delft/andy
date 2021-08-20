package nl.tudelft.cse1110.andy.grader.execution.step.helper;

import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;

public class AdditionalReportJUnitListener implements TestExecutionListener {

    private ResultBuilder result;

    public AdditionalReportJUnitListener(ResultBuilder result) {
        this.result = result;
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        this.result.logAdditionalReport(testIdentifier, entry);
    }
}
