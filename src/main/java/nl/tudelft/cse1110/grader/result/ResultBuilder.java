package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import org.jacoco.core.analysis.IClassCoverage;
import nl.tudelft.cse1110.grader.util.ImportUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import static javax.tools.Diagnostic.Kind.ERROR;

public class ResultBuilder {

    private boolean failed;
    private StringBuilder result = new StringBuilder();
    private StringBuilder debug = new StringBuilder();
    private Map<TestIdentifier, ReportEntry> additionalReports = new HashMap<>();

    private int testsRan = 0;
    private int testsSucceeded = 0;

    private GradeCalculator gradeCalculator;
    private GradeValues gradeValues;

    public ResultBuilder() {
        this.gradeCalculator = new GradeCalculator();
    }

    public void setGradeValues(GradeValues gradeValues) {
        this.gradeValues = gradeValues;
        this.gradeCalculator.setGradeValues(gradeValues);
    }

    public void compilationSuccess() {
        l("--- Compilation\nSuccess");
    }

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        l("We could not compile your code. See the compilation errors below:");
        for(Diagnostic diagnostic: diagnostics) {
            if(diagnostic.getKind() == ERROR) {
                l(String.format("- line %d: %s",
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));

                Optional<String> importLog = ImportUtils.checkMissingImport(diagnostic.getMessage(null));
                importLog.ifPresent(this::l);
            }
        }
        failed();
    }

    public void logFinish(ExecutionStep step) {
        d(String.format("%s finished at %s", step.getClass().getSimpleName(), now()));
    }

    public void logStart(ExecutionStep step) {
        d(String.format("%s started at %s", step.getClass().getSimpleName(), now()));
    }
    public void logFinish() {
        d(String.format("Finished at %s", now()));
    }

    public void logStart() {
        d(String.format("Started at %s", now()));
    }

    public void debug(ExecutionStep step, String msg) {
        d(String.format("%s: %s", step.getClass().getSimpleName(), msg));
    }

    public void genericFailure(ExecutionStep step, String msg) {
        l(msg);
        d(msg);

        failed();
    }

    public void genericFailure(ExecutionStep step, Throwable e) {

        StringBuilder failureMsg = new StringBuilder();

        failureMsg.append(String.format("Oh, we are facing a failure in %s that we cannot recover from.\n", step.getClass().getSimpleName()));
        failureMsg.append("Please, send the message below to the teaching team:\n");
        failureMsg.append("---\n");
        failureMsg.append(exceptionMessage(e));
        failureMsg.append("---\n");

        genericFailure(step, failureMsg.toString());
    }

    @NotNull
    private String exceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String messageToAppend = sw.toString().trim();
        return messageToAppend;
    }

    public void logJUnitRun(TestExecutionSummary summary) {
        if (summary.getTestsFoundCount() == 0) {
            noTestsFound();
        } else {

            l("\n--- JUnit execution");
            l(String.format("%d/%d passed", summary.getTestsSucceededCount(), summary.getTestsFoundCount()));

            for (TestExecutionSummary.Failure failure : summary.getFailures()) {
                this.logJUnitFailedTest(failure);
            }

            this.testsRan = (int) summary.getTestsStartedCount();
            this.testsSucceeded = (int) summary.getTestsSucceededCount();

            if(summary.getTestsSucceededCount() < summary.getTestsFoundCount())
                failed();
        }
    }

    private void noTestsFound() {
        l("--- Warning\nWe do not see any tests. Are you sure you wrote them?");
        failed();
    }

    public int getTestsRan() {
        return this.testsRan;
    }

    public int getTestsSucceeded() {
        return this.testsSucceeded;
    }

    public void logAdditionalReport(TestIdentifier testIdentifier, ReportEntry report) {
        this.additionalReports.put(testIdentifier, report);
    }

    private void logJUnitFailedTest(TestExecutionSummary.Failure failure) {
        UniqueId.Segment lastSegment = failure.getTestIdentifier().getUniqueIdObject().getLastSegment();

        switch (lastSegment.getType()) {
            case "test-template-invocation" -> {
                String methodName = this.getParameterizedMethodName(failure);
                l(String.format("\n- Parameterized test \"%s\", test case %s failed:", methodName, lastSegment.getValue()));
                l(String.format("%s", failure.getException()));
            }
            case "property" -> {
                l(String.format("\n- Property test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));

                if (this.additionalReports.containsKey(failure.getTestIdentifier())) {
                    l(this.additionalReports.get(failure.getTestIdentifier()).getKeyValuePairs().toString());
                }
            }
            default -> {
                l(String.format("\n- Test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));
                l(String.format("%s", failure.getException()));
            }
        }
    }

    private String getParameterizedMethodName(TestExecutionSummary.Failure failure) {
        int endIndex = failure.getTestIdentifier().getLegacyReportingName().indexOf('(');
        return failure.getTestIdentifier().getLegacyReportingName().substring(0, endIndex);
    }

    private void failed() {
        this.failed = true;
        gradeCalculator.failed();

    }

    public String buildEndUserResult() {
        return result.toString();
    }

    public String buildDebugResult() {
        return debug.toString() + "\n\n" + result.toString();
    }


    private void l(String line) {
        result.append(line);
        result.append("\n");
    }

    private void d(String line) {
        debug.append(line);
        debug.append("\n");
    }


    private String now() {
        return LocalDateTime.now().toString();
    }

    public void logFinalGrade() {

        // rounding up from 0.5...
        String grade = String.valueOf(gradeCalculator.calculateFinalGrade());

        l("\n--- Final grade");
        l(grade + "/100");
    }

    public void logPitest(CombinedStatistics stats) {

        int detectedMutations = (int)(stats.getMutationStatistics().getTotalDetectedMutations());
        int totalMutations = (int)(stats.getMutationStatistics().getTotalMutations());

        l("\n--- Mutation testing");
        l(String.format("%d/%d killed", detectedMutations, totalMutations));

        gradeValues.setMutationGrade(detectedMutations, totalMutations);

        if(detectedMutations < totalMutations)
            l("See attached report.");
    }


    public void logCodeChecks(CheckScript script) {
        l("\n--- Code checks");
        l(script.generateReport());

        int weightedChecks = script.getChecks().stream().mapToInt(check -> check.getFinalResult() ? check.getWeight() : 0).sum();
        int sumOfWeights =  script.getChecks().stream().mapToInt(c -> c.getWeight()).sum();

        gradeValues.setCheckGrade(weightedChecks, sumOfWeights);

    }


    public void logJacoco(Collection<IClassCoverage> coverages) {
        l("\n--- JaCoCo coverage");

        int totalCoveredLines = coverages.stream().mapToInt(coverage -> coverage.getLineCounter().getCoveredCount()).sum();
        int totalLines = coverages.stream().mapToInt(coverage -> coverage.getLineCounter().getTotalCount()).sum();

        int totalCoveredInstructions = coverages.stream().mapToInt(coverage -> coverage.getInstructionCounter().getCoveredCount()).sum();
        int totalInstructions = coverages.stream().mapToInt(coverage -> coverage.getInstructionCounter().getTotalCount()).sum();

        int totalCoveredBranches = coverages.stream().mapToInt(coverage -> coverage.getBranchCounter().getCoveredCount()).sum();
        int totalBranches = coverages.stream().mapToInt(coverage -> coverage.getBranchCounter().getTotalCount()).sum();

        l(String.format("Line coverage: %d/%d", totalCoveredLines, totalLines));
        l(String.format("Instruction coverage: %d/%d", totalCoveredInstructions, totalInstructions));
        l(String.format("Branch coverage: %d/%d", totalCoveredBranches, totalBranches));
        l("See the attached report.");
        gradeValues.setMutationGrade(totalCoveredBranches, totalBranches);
    }

    public void logMetaTests(int score, int totalTests, List<String> failures) {
        l("\n--- Meta tests");
        l(String.format("%d/%d passed", score, totalTests));
        for (String failure : failures) {
            l(String.format("Meta test: %s FAILED", failure));
        }

        gradeValues.setMetaGrade(score, totalTests);
    }

    public boolean isFailed() {
        return failed;
    }
}
