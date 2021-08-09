package nl.tudelft.cse1110.grader.execution;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

public interface ExecutionStep {
    void execute(Configuration cfg, ResultBuilder result);
}
