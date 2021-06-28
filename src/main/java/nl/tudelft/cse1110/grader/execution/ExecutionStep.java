package nl.tudelft.cse1110.grader.execution;

import nl.tudelft.cse1110.grader.result.ResultBuilder;
import nl.tudelft.cse1110.grader.config.Configuration;

public interface ExecutionStep {
    void execute(Configuration cfg, ResultBuilder result);
}
