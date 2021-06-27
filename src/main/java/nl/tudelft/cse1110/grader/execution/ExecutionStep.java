package nl.tudelft.cse1110.grader.execution;

public interface ExecutionStep {

    void execute(Configuration cfg, ExecutionFlow flow, ResultBuilder result);
}
