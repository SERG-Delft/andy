package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.execution.step.*;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static nl.tudelft.cse1110.andy.grader.result.OutputGenerator.*;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder result;
    private LinkedList<ExecutionStep> steps;

    private ExecutionFlow(List<ExecutionStep> plan, Context ctx, ResultBuilder result) {
        this.steps = new LinkedList<>(plan);
        steps.addAll(0, basicSteps());
        this.ctx = ctx;
        this.result = result;
    }

    public void run() {
        result.logStart();
        do {
            ExecutionStep currentStep = steps.pollFirst();

            result.logStart(currentStep);
            try {
                currentStep.execute(ctx, result);
            } catch(Throwable e) {
                result.genericFailure(currentStep, e);
            }
            result.logFinish(currentStep);
        } while(!steps.isEmpty() && !result.isFailed());
        result.logFinish();
        generateOutput();
    }

    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    /* In this method we also calculate the total time in seconds our tool took to run.
     */
    private void generateOutput() {
        result.logTimeToRun(ctx.getStartTime());

        exportOutputFile(ctx, result);
        exportXMLFile(ctx, result);
        if(result.containsCompilationErrors()) {
            exportCompilationHighlights(ctx, result.getCompilationErrors());
        }
    }

    public static ExecutionFlow asSteps(List<ExecutionStep> plan, Context cfg, ResultBuilder result) {
        return new ExecutionFlow(plan, cfg, result);
    }

    public static ExecutionFlow justTests(Context cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(new RunJUnitTestsStep(), new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    public static ExecutionFlow justBasic(Context cfg, ResultBuilder result) {
        return new ExecutionFlow(Collections.emptyList(), cfg, result);
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }


}
