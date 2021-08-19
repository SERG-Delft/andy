package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.grader.grade.GradeCalculator;
import nl.tudelft.cse1110.grader.grade.GradeValues;
import nl.tudelft.cse1110.grader.grade.GradeWeight;
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

    private int mutationsToConsider;

    private GradeCalculator gradeCalculator; // will be set once weights are injected
    private GradeWeight gradeWeights; // will be injected once configuration is loaded
    private GradeValues grades = new GradeValues();

    private List<Diagnostic<? extends JavaFileObject>> compilationErrors;



    public void compilationSuccess() {
        l("--- Compilation\nSuccess");
    }

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> compilationErrors) {
        this.compilationErrors = compilationErrors;

        l("We could not compile your code. See the compilation errors below:");
        for(Diagnostic diagnostic: compilationErrors) {
            if (diagnostic.getKind() == ERROR) {
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

    public void logTimeToRun(String elapsedTime) {
        l("\nOur grader took " + elapsedTime + " seconds to assess your question.");
    }

    private String getParameterizedMethodName(TestExecutionSummary.Failure failure) {
        int endIndex = failure.getTestIdentifier().getLegacyReportingName().indexOf('(');
        return failure.getTestIdentifier().getLegacyReportingName().substring(0, endIndex);
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

    public int getFinalScore(){
        return isFailed() ? -1 : finalGrade();
    }

    private int finalGrade() {
        // this might be called when compilation fails, and we have no grade calculator yet
        if (gradeCalculator == null)
            return 0;

        return gradeCalculator.calculateFinalGrade(grades);
    }

    public void logFinalGrade() {

        // rounding up from 0.5...
        String grade = String.valueOf(finalGrade());

        l("\n--- Final grade");
        l(grade + "/100");
    }

    public void logConsoleOutput(ByteArrayOutputStream console){

        l("\n--- Console output");
        l(console.toString());
    }

    public void logPitest(CombinedStatistics stats) {

        int detectedMutations = (int)(stats.getMutationStatistics().getTotalDetectedMutations());

        int totalMutations;
        boolean numberOfMutantsToConsiderIsOverridden = this.mutationsToConsider != -1;
        if (numberOfMutantsToConsiderIsOverridden) {
            totalMutations = this.mutationsToConsider;
        } else {
            totalMutations = (int)(stats.getMutationStatistics().getTotalMutations());
        }

        l("\n--- Mutation testing");
        l(String.format("%d/%d killed", detectedMutations, totalMutations));

        grades.setMutationGrade(detectedMutations, totalMutations);

        if(detectedMutations < totalMutations)
            l("See the attached report.");
      
    }


    public void logCodeChecks(CheckScript script) {

        if(script.hasChecks()) {

            l("\n--- Code checks");
            l(script.generateReportOFailedChecks().trim());

            int weightedChecks = script.weightedChecks();
            int sumOfWeights = script.weights();
            l(String.format("\nCode checks score: %d/%d", weightedChecks, sumOfWeights));

            grades.setCheckGrade(weightedChecks, sumOfWeights);
        }

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
        grades.setMutationGrade(totalCoveredBranches, totalBranches);
    }

    public void logMetaTests(int score, int totalTests, List<String> failures) {
        l("\n--- Meta tests");
        l(String.format("%d/%d passed", score, totalTests));
        for (String failure : failures) {
            l(String.format("Meta test: %s FAILED", failure));
        }

        grades.setMetaGrade(score, totalTests);
    }

    public void setGradeWeights(GradeWeight gradeWeights) {
        this.gradeWeights = gradeWeights;
        this.gradeCalculator = new GradeCalculator(gradeWeights);
    }

    public void setNumberOfMutationsToConsider(int numberOfMutations) {
        this.mutationsToConsider = numberOfMutations;
    }

    public boolean isFailed() {
        return failed;
    }

    public void failed() {
        this.failed = true;

        // The failing can happen before we instantiated a grade calculator
        // e.g., during compilation time.
        if(gradeCalculator!=null)
            gradeCalculator.failed();
    }

    public boolean containsCompilationErrors() {
        return compilationErrors!=null;
    }

    public List<Diagnostic<? extends JavaFileObject>> getCompilationErrors() {
        return compilationErrors;
    }
}
