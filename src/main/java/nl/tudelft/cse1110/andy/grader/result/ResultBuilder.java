package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.step.helper.ModeActionSelector;
import nl.tudelft.cse1110.andy.grader.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.grader.grade.GradeValues;
import nl.tudelft.cse1110.andy.grader.grade.GradeWeight;
import nl.tudelft.cse1110.andy.grader.util.ImportUtils;
import org.jacoco.core.analysis.IClassCoverage;
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
    private ModeActionSelector modeActionSelector;
    private RandomAsciiArtGenerator asciiArtGenerator;

    private List<Diagnostic<? extends JavaFileObject>> compilationErrors;


    // Facilitates testing
    public ResultBuilder(RandomAsciiArtGenerator asciiArtGenerator, GradeCalculator calculator) {
        this.asciiArtGenerator = asciiArtGenerator;
        this.gradeCalculator = calculator;
    }

    public ResultBuilder() {
        this.asciiArtGenerator = new RandomAsciiArtGenerator();
    }

    public void setModeSelector(ModeActionSelector modeActionSelector) {
        this.modeActionSelector = modeActionSelector;
    }


    public void compilationSuccess() {
        l("--- Compilation\nSuccess");
    }

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> compilationErrors) {
        this.compilationErrors = compilationErrors;

        l("We could not compile the code. See the compilation errors below:");
        for(Diagnostic diagnostic: compilationErrors) {
            if (diagnostic.getKind() == ERROR) {

                l(String.format("- line %d: %s",
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));

                Optional<String> importLog = ImportUtils.checkMissingImport(diagnostic.getMessage(null));
                importLog.ifPresent(this::l);
            }
        }

        if(anyOfTheErrorsAreCausedDueToBadConfiguration(compilationErrors)) {
            l("\n**WARNING:** There might be a problem with this exercise. "+
                    "Please contact the teaching staff so they can fix this as quickly" +
                    "as possible. Thank you for your help! :)");
        }

        failed();
    }

    private boolean anyOfTheErrorsAreCausedDueToBadConfiguration(List<Diagnostic<? extends JavaFileObject>> compilationErrors) {
        return compilationErrors
                .stream()
                .anyMatch(c -> c.getKind() == ERROR && c.getSource().getName().endsWith("Configuration.java"));
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

    public void genericFailure(String msg) {
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

        genericFailure(failureMsg.toString());
    }

    private String exceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String messageToAppend = sw.toString().trim();
        return messageToAppend;
    }

    public void logJUnitRun(TestExecutionSummary summary) {
        if (summary.getTestsFoundCount() == 0) {
            noTestsFound(summary);
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

    /** Checks for different error cases possible when tests are not detected
     * @param summary - JUnit execution summary
     */
    private void noTestsFound(TestExecutionSummary summary) {

        if (summary.getContainersFoundCount() > summary.getContainersStartedCount()) {
            l("--- Warning\nWe do not see any tests.\n" +
                    "Please check for the following JUnit pre-conditions:\n" +
                    "- @BeforeAll and @AfterAll methods should be static\n" +
                    "- @BeforeEach methods should be non-static\n");
        } else {
            l("--- Warning\nWe do not see any tests.\n" +
                    "Please check for the following JUnit pre-conditions:\n" +
                    "- Normal tests must be annotated with \"@Test\"\n" +
                    "- Parameterized tests must be annotated with \"@ParameterizedTest\"\n" +
                    "- Method sources must be static and provided as: \"@MethodSource(\"generator\")\" e.g.\n" +
                    "- Property based tests must be annotated with \"@Property\"\n");
        }
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
        boolean isParameterizedTest = failure.getTestIdentifier().getUniqueId().contains("test-template-invocation");
        boolean isPBT = failure.getTestIdentifier().getUniqueId().contains("property");

        if(isParameterizedTest) {
            String methodName = this.getParameterizedMethodName(failure);
            String testCaseNumber = this.getParameterizedTestCaseNumber(failure);
            l(String.format("\n- Parameterized test \"%s\", test case #%s failed:", methodName, testCaseNumber));
            l(String.format("%s", failure.getException()));
        } else if (isPBT) {
            l(String.format("\n- Property test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));

            if (this.additionalReports.containsKey(failure.getTestIdentifier())) {
                l(this.additionalReports.get(failure.getTestIdentifier()).getKeyValuePairs().toString());
            }
        } else {
            l(String.format("\n- Test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));
            l(String.format("%s", simplifyTestErrorMessage(failure)));
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

    public void logMode() {
        // modeActionSelector can be null if the code did not get to GetRunConfigurationStep.
        // This can happen due to a compilation error for example.
        if (modeActionSelector != null) {
            l(String.format("\nAndy is running in %s mode.", modeActionSelector.getMode().toString()));
        }
    }

    public void logTimeToRun(long startTime) {
        long stopTime = System.nanoTime();
        long elapsedTime = stopTime - startTime;
        double timeInSeconds = (double) elapsedTime / 1_000_000_000.0;

        l(String.format("\nAndy took %.1f seconds to assess your question.", timeInSeconds));
    }

    public String buildEndUserResult() {
        return result.toString();
    }

    public String buildDebugResult() {
        return debug.toString();
    }

    private void l(String line) {
        result.append(line);
        result.append("\n");

        debug.append(line);
        debug.append("\n");
    }

    private void d(String line) {
        debug.append("DEBUG: " + line);
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
        l(grade + "/100\n");

        if (finalGrade() == 100) {
            printAsciiArt();
        }
    }

    private void printAsciiArt() {
        String randomAsciiArt = asciiArtGenerator.getRandomAsciiArt();
        l(randomAsciiArt);
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
      
    }

    public void logCodeChecks(CheckScript script) {

        if (script.hasChecks()) {
            int weightedChecks = script.weightedChecks();
            int sumOfWeights = script.weights();

            if (modeActionSelector.shouldShowHints()) {
                l("\n--- Code checks");
                l(script.generateReportOFailedChecks().trim());

                l(String.format("\n%d/%d passed", weightedChecks, sumOfWeights));
            }

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

        grades.setBranchGrade(totalCoveredBranches, totalBranches);
    }

    public void logMetaTests(int score, int totalTests, List<String> passes, List<String> failures) {
        if (modeActionSelector.shouldShowHints()) {
            l("\n--- Meta tests");
            l(String.format("%d/%d passed", score, totalTests));
            for (String pass : passes) {
                l(String.format("Meta test: %s PASSED", pass));
            }
            for (String failure : failures) {
                l(String.format("Meta test: %s FAILED", failure));
            }
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
        if (gradeCalculator != null)
            gradeCalculator.failed();
    }

    public boolean containsCompilationErrors() {
        return compilationErrors!=null;
    }

    public List<Diagnostic<? extends JavaFileObject>> getCompilationErrors() {
        return compilationErrors;
    }
}
