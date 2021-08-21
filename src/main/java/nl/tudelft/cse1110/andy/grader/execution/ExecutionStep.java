package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

public interface ExecutionStep {
    void execute(Context ctx, ResultBuilder result);
}
