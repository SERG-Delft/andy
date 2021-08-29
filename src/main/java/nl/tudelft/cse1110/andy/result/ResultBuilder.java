package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckType;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.grade.GradeValues;
import nl.tudelft.cse1110.andy.grade.GradeWeight;
import nl.tudelft.cse1110.andy.utils.ImportUtils;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.IMethodCoverage;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static javax.tools.Diagnostic.Kind.ERROR;
import static nl.tudelft.cse1110.andy.utils.JUnitUtils.*;

public class ResultBuilder {

    private boolean failed;

    private StringBuilder result = new StringBuilder();
    private StringBuilder debug = new StringBuilder();

    // junit callback so that we get extra info from the test runs
    private Map<TestIdentifier, ReportEntry> additionalReports = new HashMap<>();

    private ModeActionSelector modeActionSelector; // will be injected once configuration is loaded
    private int mutationsToConsider; // will be injected once configuration is loaded
    private RandomAsciiArtGenerator asciiArtGenerator;

    // grade related stuff
    private GradeCalculator gradeCalculator; // will be set once weights are injected
    private GradeWeight gradeWeights; // will be injected later once run configuration is loaded
    private GradeValues grades = new GradeValues();

    // the list of things to be highlighted later in the IDE
    private List<Highlight> highlights = new ArrayList<>();

    // it will be set in case of a generic failure. It one happens, the output is purely the generic failure
    private String genericFailureMessage;

    // results
    private List<String> metaTestPasses;
    private List<String> metaTestFailures;
    private int totalCoveredBranches;
    private int totalBranches;
    private int totalMutants;
    private int coveredMutants;
    private List<CheckType> codeChecks;
    private int testsRan = 0;
    private int testsSucceeded = 0;


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
        l("We could not compile the code. See the compilation errors below:");
        for(Diagnostic diagnostic: compilationErrors) {
            if (diagnostic.getKind() == ERROR) {

                String message = diagnostic.getMessage(null);
                long lineNumber = diagnostic.getLineNumber();
                l(String.format("- line %d: %s",
                        lineNumber,
                        message));

                Optional<String> importLog = ImportUtils.checkMissingImport(message);
                importLog.ifPresent(this::l);

                highlights.add(new Highlight(lineNumber, message, Highlight.HighlightLocation.SOLUTION, Highlight.HighlightPurpose.COMPILATION_ERROR));
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
        this.genericFailureMessage = msg;
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

            if(summary.getTestsSucceededCount() < summary.getTestsFoundCount()) {
                l("You must ensure that all tests are passing! Stopping the assessment.");
                failed();
            }
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

    public void logAdditionalReport(TestIdentifier testIdentifier, ReportEntry report) {
        this.additionalReports.put(testIdentifier, report);
    }

    private void logJUnitFailedTest(TestExecutionSummary.Failure failure) {
        boolean isParameterizedTest = failure.getTestIdentifier().getUniqueId().contains("test-template-invocation");
        boolean isPBT = failure.getTestIdentifier().getUniqueId().contains("property");

        if(isParameterizedTest) {
            String methodName = getParameterizedMethodName(failure);
            String testCaseNumber = getParameterizedTestCaseNumber(failure);
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
        boolean wasItAGenericFailure = genericFailureMessage != null;
        return isFailed() && wasItAGenericFailure ? genericFailureMessage : result.toString();
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

    public int finalGrade() {
        // this might be called when compilation fails, and we have no grade calculator yet
        if (gradeCalculator == null)
            return 0;

        // check whether we should actually calculate the grades, depending on the current mode and action
        if(!modeActionSelector.shouldCalculateAndShowGrades())
            return 0;

        return gradeCalculator.calculateFinalGrade(grades, failed);
    }

    public void logFinalGrade() {
        // we only show grades in specific modes and actions
        // if ModeActionSelector is not injected yet (i.e., it's null), it's because compilation fail.
        // in this case, we give it a zero, no matter the mode.
        boolean thereIsAModeActionSelector = modeActionSelector != null;
        boolean shouldShowGrades = thereIsAModeActionSelector ? modeActionSelector.shouldCalculateAndShowGrades() : true;
        if(!shouldShowGrades)
            return;

        int finalGrade = finalGrade();

        l("");
        l("--- Final grade");

        // describe the weights and grades per component
        if(finalGrade > 0) {
            printGradeCalculationDetails();
        }

        // print the final grade
        l("");
        l(String.format("Final grade: %d/100", finalGrade));

        // print some nice ascii art if it's full grade!
        boolean fullyCorrect = finalGrade == 100;
        if (fullyCorrect) {
            String randomAsciiArt = asciiArtGenerator.getRandomAsciiArt();
            l(randomAsciiArt);
        }
    }

    private void printGradeCalculationDetails() {
        l(String.format("Branch coverage: %d/%d (overall weight=%.2f)%s", grades.getCoveredBranches(), grades.getTotalBranches(), gradeWeights.getBranchCoverageWeight(),
                (grades.getTotalBranches() == 0 && gradeWeights.getBranchCoverageWeight()==0 ? " (0 gives full points)":"") ));
        l(String.format("Mutation coverage: %d/%d (overall weight=%.2f)%s", grades.getDetectedMutations(), grades.getTotalMutations(), gradeWeights.getMutationCoverageWeight(),
                (grades.getTotalMutations() == 0 && gradeWeights.getMutationCoverageWeight()==0 ? " (0 gives full points)":"")));
        l(String.format("Code checks: %d/%d (overall weight=%.2f)%s", grades.getChecksPassed(), grades.getTotalChecks(), gradeWeights.getCodeChecksWeight(),
                (grades.getTotalChecks() == 0 && gradeWeights.getCodeChecksWeight()==0 ? " (0 gives full points)":"")));
        l(String.format("Meta tests: %d/%d (overall weight=%.2f)%s", grades.getMetaTestsPassed(), grades.getTotalMetaTests(), gradeWeights.getMetaTestsWeight(),
                (grades.getTotalMetaTests() == 0 && gradeWeights.getMetaTestsWeight()==0 ? " (0 gives full points)":"")));
    }

    public void logConsoleOutput(String console){
        l("\n--- Console output");
        l(console);
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

        this.totalMutants = totalMutations;
        this.coveredMutants = detectedMutations;
      
    }

    public void logCodeChecks(CheckScript script) {

        if (script.hasChecks()) {
            int weightedChecks = script.weightedChecks();
            int sumOfWeights = script.weights();

            if (modeActionSelector.shouldShowHints()) {
                l("\n--- Code checks");
                l(script.generateReportOfFailedChecks().trim());
                l(String.format("\n%d/%d passed", weightedChecks, sumOfWeights));
            }

            grades.setCheckGrade(weightedChecks, sumOfWeights);

            this.codeChecks = script.getChecks();
        } else {
            if(modeActionSelector.shouldShowHints()) {
                l("\n--- Code checks");
                l("No code checks to be assessed");
            }
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

        highlightCoverage(coverages);

        this.totalBranches = totalBranches;
        this.totalCoveredBranches = totalCoveredBranches;

        grades.setBranchGrade(totalCoveredBranches, totalBranches);
    }

    private void highlightCoverage(Collection<IClassCoverage> coverages) {
        List<Integer> partiallyCoveredLines = new ArrayList<>();
        List<Integer> notCoveredLines = new ArrayList<>();

        for (IClassCoverage coverage : coverages) {
            for (IMethodCoverage method : coverage.getMethods()) {
                for(int line = method.getFirstLine(); line <= method.getLastLine(); line++) {
                    int totalBranches = method.getLine(line).getBranchCounter().getTotalCount();
                    int missedBranches = method.getLine(line).getBranchCounter().getMissedCount();
                    boolean lineTouched = method.getLine(line).getInstructionCounter().getTotalCount() == 0 || method.getLine(line).getInstructionCounter().getCoveredCount() > 0;

                    boolean fullCoverage = lineTouched && missedBranches == 0;
                    boolean partialCoverage = lineTouched && missedBranches > 0 && totalBranches > 0;

                    if(fullCoverage) {
                        highlights.add(new Highlight(line, "100% coverage",
                                Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.FULL_COVERAGE));
                    } else if(partialCoverage) {
                        highlights.add(new Highlight(line, "Partial coverage",
                                Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.PARTIAL_COVERAGE));
                        partiallyCoveredLines.add(line);
                    } else {
                        highlights.add(new Highlight(line, "No coverage",
                                Highlight.HighlightLocation.LIBRARY, Highlight.HighlightPurpose.NO_COVERAGE));
                        notCoveredLines.add(line);
                    }
                }
            }
        }

        if(!partiallyCoveredLines.isEmpty()) {
            l(String.format("Partially covered lines: %s",
                    partiallyCoveredLines.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }
        if(!notCoveredLines.isEmpty()) {
            l(String.format("Lines not covered: %s",
                    notCoveredLines.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", "))));
        }
    }

    public void logMetaTests(int score, int totalTests, List<String> passes, List<String> failures) {

        this.metaTestPasses = passes;
        this.metaTestFailures = failures;

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
    }

    public List<Highlight> getHighlights() {
        return Collections.unmodifiableList(highlights);
    }

    public List<String> getFailingMetaTests() {
        if(metaTestFailures==null)
            return Collections.emptyList();
        return metaTestFailures;
    }

    public List<String> getPassingMetaTests() {

        if(metaTestPasses == null)
            return Collections.emptyList();

        return metaTestPasses;
    }

    public int getTotalBranches() {
        return totalBranches;
    }

    public int getBranchesCovered() {
        return totalCoveredBranches;
    }

    public int getTotalMutants() {
        return totalMutants;
    }

    public int getCoveredMutants() {
        return coveredMutants;
    }

    public List<CheckType> getCodeChecks() {
        if(codeChecks == null)
            return Collections.emptyList();

        return Collections.unmodifiableList(codeChecks);
    }

    public int getTestsRan() {
        return this.testsRan;
    }

    public int getTestsSucceeded() {
        return this.testsSucceeded;
    }

}
