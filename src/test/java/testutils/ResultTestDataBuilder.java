package testutils;

import nl.tudelft.cse1110.andy.grade.GradeWeight;
import nl.tudelft.cse1110.andy.result.*;

import java.util.Arrays;

public class ResultTestDataBuilder {

    private String genericFailureMessage = null;
    private CompilationResult compilation = CompilationResult.empty();
    private UnitTestsResult testResults = UnitTestsResult.empty();
    private MutationTestingResult mutationResults = MutationTestingResult.empty();
    private CodeChecksResult codeCheckResults = CodeChecksResult.empty();
    private CoverageResult coverageResults = CoverageResult.empty();
    private MetaTestsResult metaTestResults = MetaTestsResult.empty();
    private int finalGrade = 0;
    private double timeInSeconds = 10;
    private GradeWeight weights = new GradeWeight(0.25f, 0.25f, 0.25f, 0.25f);

    public ResultTestDataBuilder withCompilationFail(CompilationErrorInfo... errors) {
        compilation = CompilationResult.compilationFail(Arrays.asList(errors));
        return this;
    }

    public Result build() {
        return new Result(compilation, testResults, mutationResults, codeCheckResults, coverageResults, metaTestResults, finalGrade, genericFailureMessage, timeInSeconds, weights);
    }
}
