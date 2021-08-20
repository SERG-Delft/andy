package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;
import nl.tudelft.cse1110.andy.grader.config.Configuration;

public interface ExecutionStep {
    void execute(Configuration cfg, ResultBuilder result);
}
