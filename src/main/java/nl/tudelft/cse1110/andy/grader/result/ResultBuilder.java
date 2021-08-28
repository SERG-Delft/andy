package nl.tudelft.cse1110.andy.grader.result;

import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.execution.mode.ModeActionSelector;
import nl.tudelft.cse1110.andy.grader.grade.GradeValues;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultBuilder {
    private StringBuilder result = new StringBuilder();
    private StringBuilder debug = new StringBuilder();
    private List<Highlight> highlights = new ArrayList<>();
    private GradeValues grades = new GradeValues();

    private boolean failed;
    private RandomAsciiArtGenerator asciiArtGenerator;

    // it will be set in case of a generic failure. It one happens, the output is purely the generic failure
    private String genericFailureMessage;

    // we keep the number of tests ran and succeeded for the meta tests step,
    // as the step needs it to know whether the meta test passed or not
    private int testsRan;
    private int testsSucceeded;

    // final grade is set after the CalculateFinalGradeStep
    private int finalGrade;

    // Facilitates testing
    public ResultBuilder(RandomAsciiArtGenerator asciiArtGenerator) {
        this.asciiArtGenerator = asciiArtGenerator;
    }

    public ResultBuilder() {
        this.asciiArtGenerator = new RandomAsciiArtGenerator();
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

    public void logMode(ModeActionSelector modeActionSelector) {
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

    public int getFinalScore(){
        return isFailed() ? -1 : finalGrade;
    }

    public void logConsoleOutput(String console){
        l("\n--- Console output");
        l(console);
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

    public void message(String line) {
        l(line);
    }

    public void highlight(Highlight highlight) {
        highlights.add(highlight);
    }

    public void setMutationGrade(int detectedMutations, int totalMutations) {
        grades.setMutationGrade(detectedMutations, totalMutations);
    }

    public void setJUnitResults(int testsRan, int testsSucceeded) {
        this.testsRan = testsRan;
        this.testsSucceeded = testsSucceeded;
    }

    public void setBranchGrade(int totalCoveredBranches, int totalBranches) {
        grades.setBranchGrade(totalCoveredBranches, totalBranches);
    }

    public void setCheckGrade(int weightedChecks, int sumOfWeights) {
        grades.setCheckGrade(weightedChecks, sumOfWeights);
    }

    public void setMetaGrade(int score, int totalTests) {
        grades.setMetaGrade(score, totalTests);
    }

    public int getTestsSucceeded() {
        return testsSucceeded;
    }

    public int getTestsRan() {
        return testsRan;
    }

    public GradeValues grades() {
        return grades;
    }

    public void printRandomAsciiArt() {
        String randomAsciiArt = asciiArtGenerator.getRandomAsciiArt();
        l(randomAsciiArt);
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }
}
