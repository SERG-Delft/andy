package nl.tudelft.cse1110.andy.execution;

import nl.tudelft.cse1110.andy.execution.step.*;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.writer.ResultWriter;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps;
    private final ResultWriter writer;

    private ExecutionFlow(List<ExecutionStep> plan, Context ctx, ResultBuilder result, ResultWriter writer) {
        this.steps = new LinkedList<>(plan);
        this.writer = writer;
        steps.addAll(0, basicSteps());
        this.ctx = ctx;
        this.result = result;
        this.ctx.setFlow(this);
    }

    public void run() {
        do {
            ExecutionStep currentStep = steps.pollFirst();
            try {
                currentStep.execute(ctx, result);
            } catch(Throwable e) {
                result.genericFailure(currentStep, e);
            }
        } while(!steps.isEmpty() && !result.hasFailed());

        Result solutionResult = result.build();
        writer.write(solutionResult);
    }


    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    public static ExecutionFlow build(Context ctx, ResultBuilder result, ResultWriter writer) {
        return new ExecutionFlow(Collections.emptyList(), ctx, result, writer);
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }


}
