package nl.tudelft.cse1110.andy.grader.analytics;

import java.time.LocalDate;
import java.util.List;

public class Submission {

    private final SubmissionMetaData metaData;

    private final LocalDate time;
    private final boolean compilationOk;
    private final int finalGrade;

    private final SubmissionTest tests;
    private final List<SubmissionMetaTest> metaTests;
    private final List<SubmissionCodeCheck> codeChecks;
    private final SubmissionCoverage coverage;

    public Submission(SubmissionMetaData metaData, LocalDate time, boolean compilationOk, int finalGrade, SubmissionTest tests, List<SubmissionMetaTest> metaTests, List<SubmissionCodeCheck> codeChecks, SubmissionCoverage coverage) {
        this.metaData = metaData;
        this.time = time;
        this.compilationOk = compilationOk;
        this.finalGrade = finalGrade;
        this.tests = tests;
        this.metaTests = metaTests;
        this.codeChecks = codeChecks;
        this.coverage = coverage;
    }

    public int getFinalGrade() {
        return finalGrade;
    }
}
