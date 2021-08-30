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
    private final double timeInSeconds;
    private final GradeWeight weights;

    public Result(CompilationResult compilation, UnitTestsResult tests, MutationTestingResult mutationTesting, CodeChecksResult codeChecks, CoverageResult coverage, MetaTestsResult metaTests, int finalGrade, String genericFailure, double timeInSeconds, GradeWeight weights) {
        this.compilation = compilation;
        this.tests = tests;
        this.mutationTesting = mutationTesting;
        this.codeChecks = codeChecks;
        this.coverage = coverage;
        this.metaTests = metaTests;
        this.finalGrade = finalGrade;
        this.genericFailure = genericFailure;
        this.timeInSeconds = timeInSeconds;
        this.weights = weights;
    }

    public Result(CompilationResult compilation, double timeInSeconds) {
        this(compilation, UnitTestsResult.empty(), MutationTestingResult.empty(), CodeChecksResult.empty(), CoverageResult.empty(), MetaTestsResult.empty(), 0, null, timeInSeconds, null);
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

    public boolean hasFailed() {
        return !compilation.successful() || tests.hasFailingTests() || genericFailure!=null;
    }
}
