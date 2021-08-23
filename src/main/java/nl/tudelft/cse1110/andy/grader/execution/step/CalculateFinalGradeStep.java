package nl.tudelft.cse1110.andy.grader.execution.step;

import nl.tudelft.cse1110.andy.grader.execution.Context;
import nl.tudelft.cse1110.andy.grader.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

public class CalculateFinalGradeStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        result.logFinalGrade();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof CalculateFinalGradeStep;
    }
}
