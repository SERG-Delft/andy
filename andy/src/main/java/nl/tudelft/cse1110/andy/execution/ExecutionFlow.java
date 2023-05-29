package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.execution.step.*;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps;
    private final ResultWriter writer;

    public ExecutionFlow(Context ctx, ResultBuilder result, ResultWriter writer, List<ExecutionStep> steps) {
        this.steps = new LinkedList<>();

        this.steps.addAll(0, steps);

        this.writer = writer;
        this.ctx = ctx;
        this.result = result;
        this.ctx.setFlow(this);
    }

    public void run() {
        try {
            do {
                ExecutionStep currentStep = steps.pollFirst();
                try {
                    currentStep.execute(ctx, result);
                } catch (Throwable e) {
                    result.genericFailure(currentStep, e);
                }
            } while (!steps.isEmpty() && !result.hasFailed());

            ctx.killExternalProcess();

            Result solutionResult = result.build();
            writer.write(ctx, solutionResult);
        } catch (Throwable t) {
            // in case something even totally unexpected happens, we log it.
            writer.uncaughtError(ctx, t);
        }
    }

    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    public static ExecutionFlow build(Context ctx, ResultBuilder result, ResultWriter writer) {
        return new ExecutionFlow(ctx, result, writer, basicSteps());
    }

    private static List<ExecutionStep> basicSteps() {
        return Arrays.asList(
                new OrganizeSourceCodeStep(),
                new SourceCodeSecurityCheckStep(),
                new CompilationStep(),
                new ReplaceClassloaderStep(),
                new GetRunConfigurationStep(),
                new InjectModeActionStepsStep());
    }
}
