package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.step.*;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder resultBuilder;
    private LinkedList<ExecutionStep> steps;

    private ExecutionFlow(Context ctx, List<ExecutionStep> steps) {
        this.steps = new LinkedList<>();

        this.steps.addAll(0, steps);

        this.ctx = ctx;
        this.resultBuilder = new ResultBuilder(ctx, new GradeCalculator());
        this.ctx.setFlow(this);
    }

    public Result run() {
        do {
            ExecutionStep currentStep = steps.pollFirst();
            try {
                currentStep.execute(ctx, resultBuilder);
            } catch (Throwable e) {
                resultBuilder.genericFailure(currentStep, e);
            }
        } while (!steps.isEmpty() && !resultBuilder.hasFailed());

        ctx.killExternalProcess();

        return resultBuilder.build();
    }

    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    public void addStepAsNext(ExecutionStep step) {
        this.steps.add(0, step);
    }

    public static ExecutionFlow build(Context ctx) {
        return new ExecutionFlow(ctx, basicSteps());
    }

    public static ExecutionFlow build(Context ctx, List<ExecutionStep> steps) {
        return new ExecutionFlow(ctx, steps);
    }

    public static List<ExecutionStep> basicSteps() {
        return Arrays.asList(
                new OrganizeSourceCodeStep(),
                new SourceCodeSecurityCheckStep(),
                new CompilationStep(),
                new ReplaceClassloaderStep(),
                new GetRunConfigurationStep(),
                new ExamModeSecurityGuardStep(),
                new InjectModeActionStepsStep());
    }
}
