package nl.tudelft.cse1110.grader.execution;

import nl.tudelft.cse1110.grader.config.Configuration;
import nl.tudelft.cse1110.grader.execution.step.*;
import nl.tudelft.cse1110.grader.result.ResultBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExecutionFlow {
    private final Configuration cfg;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps;

    private ExecutionFlow(List<ExecutionStep> plan, Configuration cfg, ResultBuilder result) {
        this.steps = new LinkedList<>(plan);
        steps.addAll(0, basicSteps());
        this.cfg = cfg;
        this.result = result;
    }

    public void run() {
        result.logStart();
        do {
            ExecutionStep currentStep = steps.pollFirst();

            result.logStart(currentStep);
            try {
                currentStep.execute(cfg, result);
            } catch(Throwable e) {
                result.genericFailure(currentStep, e);
            }
            result.logFinish(currentStep);
        } while(!steps.isEmpty() && !result.isFailed());
        result.logFinish();
    }

    public static ExecutionFlow examMode(Configuration cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(
                        new RunJUnitTests(),
                        new RunJacoco(),
                        new RunPitest(), new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    public static ExecutionFlow asSteps(List<ExecutionStep> plan, Configuration cfg, ResultBuilder result) {
        return new ExecutionFlow(plan, cfg, result);
    }

    public static ExecutionFlow fullMode(Configuration cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(
                        new RunJUnitTests(),
                        new RunJacoco(),
                        new RunPitest(),
                        new CodeChecksStep(),
                        new RunMetaTests(),
                        new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    public static ExecutionFlow justTests(Configuration cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(new RunJUnitTests(), new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }


}
