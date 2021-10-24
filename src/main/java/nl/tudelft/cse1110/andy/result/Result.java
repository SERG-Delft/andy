package nl.tudelft.cse1110.andy.result;

import nl.tudelft.cse1110.andy.grade.GradeWeight;

public class Result {

    private final CompilationResult compilation;
    private final UnitTestsResult tests;
    private final MutationTestingResult mutationTesting;
    private final CodeChecksResult codeChecks;
    private final CoverageResult coverage;
    private final MetaTestsResult metaTests;
    private final int finalGrade;
    private final String genericFailure;
    private final String genericFailureStepName;
    private final Throwable genericFailureException;
    private final Integer genericFailureExternalProcessExitCode;
    private final String genericFailureExternalProcessErrorMessages;
    private final double timeInSeconds;
    private final GradeWeight weights;

    public Result(CompilationResult compilation, UnitTestsResult tests, MutationTestingResult mutationTesting, CodeChecksResult codeChecks, CoverageResult coverage, MetaTestsResult metaTests, int finalGrade, String genericFailure, String genericFailureStepName, Throwable genericFailureException, Integer genericFailureExternalProcessExitCode, String genericFailureExternalProcessErrorMessages, double timeInSeconds, GradeWeight weights) {
        this.compilation = compilation;
        this.tests = tests;
        this.mutationTesting = mutationTesting;
        this.codeChecks = codeChecks;
        this.coverage = coverage;
        this.metaTests = metaTests;
        this.finalGrade = finalGrade;
        this.genericFailure = genericFailure;
        this.genericFailureStepName = genericFailureStepName;
        this.genericFailureException = genericFailureException;
        this.genericFailureExternalProcessExitCode = genericFailureExternalProcessExitCode;
        this.genericFailureExternalProcessErrorMessages = genericFailureExternalProcessErrorMessages;
        this.timeInSeconds = timeInSeconds;
        this.weights = weights;

        if(finalGrade < 0 || finalGrade>100)
            throw new RuntimeException("Invalid final grade");
    }

    public Result(CompilationResult compilation, double timeInSeconds) {
        this(compilation, UnitTestsResult.empty(), MutationTestingResult.empty(), CodeChecksResult.empty(), CoverageResult.empty(), MetaTestsResult.empty(), 0, null, null, null, null, null, timeInSeconds, null);
    }

    public CompilationResult getCompilation() {
        return compilation;
    }

    public UnitTestsResult getTests() {
        return tests;
    }

    public MutationTestingResult getMutationTesting() {
        return mutationTesting;
    }

    public CodeChecksResult getCodeChecks() {
        return codeChecks;
    }

    public CoverageResult getCoverage() {
        return coverage;
    }

    public MetaTestsResult getMetaTests() {
        return metaTests;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public double getTimeInSeconds() {
        return timeInSeconds;
    }

    public GradeWeight getWeights() {
        return weights;
    }

    public String getGenericFailure() {
        return genericFailure;
    }

    public String getGenericFailureStepName() {
        return genericFailureStepName;
    }

    public Throwable getGenericFailureException() {
        return genericFailureException;
    }

    public Integer getGenericFailureExternalProcessExitCode() {
        return genericFailureExternalProcessExitCode;
    }

    public String getGenericFailureExternalProcessErrorMessages() {
        return genericFailureExternalProcessErrorMessages;
    }

    public boolean hasFailed() {
        return !compilation.successful() || tests.hasTestsFailingOrFailures() || hasGenericFailure();
    }

    public boolean hasGenericFailure() {
        return genericFailure != null || genericFailureStepName != null || genericFailureException != null ||
               genericFailureExternalProcessExitCode != null || genericFailureExternalProcessErrorMessages != null;
    }

    @Override
    public String toString() {
        return "Result{" +
                "compilation=" + compilation +
                ", tests=" + tests +
                ", mutationTesting=" + mutationTesting +
                ", codeChecks=" + codeChecks +
                ", coverage=" + coverage +
                ", metaTests=" + metaTests +
                ", finalGrade=" + finalGrade +
                ", genericFailure='" + genericFailure + '\'' +
                ", timeInSeconds=" + timeInSeconds +
                ", weights=" + weights +
                '}';
    }

    public boolean isFullyCorrect() {
        return finalGrade == 100;
    }
}
