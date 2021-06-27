package nl.tudelft.cse1110.grader.execution;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.util.LinkedList;

public class ExecutionFlow {
    private final Configuration cfg;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps = new LinkedList<>();

    public ExecutionFlow(ExecutionStep firstStep, Configuration cfg, ResultBuilder result) {
        steps.add(firstStep);
        this.cfg = cfg;
        this.result = result;
    }

    public void next(ExecutionStep step) {
        steps.addLast(step);
    }

    public void run() {
        do {
            ExecutionStep currentStep = steps.pollFirst();
            result.logStart(currentStep);
            currentStep.execute(cfg, this, result);
            result.logFinish(currentStep);
        } while(!steps.isEmpty());
    }
}
