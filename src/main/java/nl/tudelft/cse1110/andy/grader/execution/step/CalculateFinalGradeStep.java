package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.config.Configuration;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

public class CalculateFinalGradeStep implements ExecutionStep {

    @Override
    public void execute(Configuration cfg, ResultBuilder result) {
        result.logFinalGrade();
    }
}
