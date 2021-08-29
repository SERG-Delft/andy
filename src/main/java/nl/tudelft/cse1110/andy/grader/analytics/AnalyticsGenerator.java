package nl.tudelft.cse1110.andy.grader.analytics;

import com.google.gson.Gson;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsGenerator {

    public String generate(ResultBuilder result) {

        Submission submission = new Submission(
                new SubmissionMetaData("course", "studentid", "studentname", "exercise"),
                LocalDate.now(),
                result.isCompilationSuccess(),
                result.finalGrade(),
                buildTests(result),
                buildMetaTests(result),
                buildCodeChecks(result),
                buildSubmissionCoverage(result)
        );

        return new Gson().toJson(submission);
    }

    private SubmissionCoverage buildSubmissionCoverage(ResultBuilder result) {
        return new SubmissionCoverage(
                result.getBranchesCovered(), result.getTotalBranches(),
                result.getCoveredMutants(), result.getTotalMutants());
    }

    private List<SubmissionCodeCheck> buildCodeChecks(ResultBuilder result) {
        if(result.getCodeChecks()==null)
            return Collections.emptyList();

        return result.getCodeChecks()
                .stream()
                .map(c -> new SubmissionCodeCheck(c.getDescription(), c.getFinalResult()))
                .collect(Collectors.toList());
    }

    private List<SubmissionMetaTest> buildMetaTests(ResultBuilder result) {
        List<SubmissionMetaTest> list = new ArrayList<>();

        if(result.getPassingMetaTests()!=null) {
            list.addAll(result.getPassingMetaTests().stream().map(x -> new SubmissionMetaTest(x, true))
                    .collect(Collectors.toList()));
        }

        if(result.getFailingMetaTests()!=null) {
            list.addAll(result.getFailingMetaTests().stream().map(x -> new SubmissionMetaTest(x, false))
                    .collect(Collectors.toList()));
        }

        return list;
    }

    private SubmissionTest buildTests(ResultBuilder result) {
        return new SubmissionTest(result.getTestsSucceeded(), result.getTestsRan());
    }
}
