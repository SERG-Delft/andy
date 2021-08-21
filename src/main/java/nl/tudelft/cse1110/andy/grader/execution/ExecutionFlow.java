package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.execution.step.*;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import java.util.Arrays;
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

    public static ExecutionFlow examMode(Context cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(
                        new RunJUnitTestsStep(),
                        new RunJacocoCoverageStep(),
                        new RunPitestStep(),
                        new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    public static ExecutionFlow asSteps(List<ExecutionStep> plan, Context cfg, ResultBuilder result) {
        return new ExecutionFlow(plan, cfg, result);
    }

    public static ExecutionFlow fullMode(Context cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(
                        new RunJUnitTestsStep(),
                        new RunJacocoCoverageStep(),
                        new RunPitestStep(),
                        new RunCodeChecksStep(),
                        new RunMetaTestsStep(),
                        new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    public static ExecutionFlow justTests(Context cfg, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(new RunJUnitTestsStep(), new CalculateFinalGradeStep()),
                cfg,
                result
        );
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }


}
