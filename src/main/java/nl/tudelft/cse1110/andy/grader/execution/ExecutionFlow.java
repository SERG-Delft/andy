package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.execution.step.*;
import nl.tudelft.cse1110.andy.grader.result.OutputGenerator;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExecutionFlow {
    private final Context ctx;
    private final ResultBuilder result;
    private final OutputGenerator outputGenerator;
    private LinkedList<ExecutionStep> steps;

    private ExecutionFlow(List<ExecutionStep> plan, Context ctx, ResultBuilder result) {
        this.steps = new LinkedList<>(plan);
        steps.addAll(0, basicSteps());
        this.ctx = ctx;
        this.result = result;
        this.ctx.setFlow(this);

        this.outputGenerator = new OutputGenerator(ctx);
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

        logFinalGrade();
        generateOutput();
    }

    private void logFinalGrade() {
        result.logFinalGrade();
    }

    public void addSteps(List<ExecutionStep> steps) {
        this.steps.addAll(steps);
    }

    /* In this method we also calculate the total time in seconds our tool took to run.
     */
    private void generateOutput() {
        result.logMode();
        result.logTimeToRun(ctx.getStartTime());

        outputGenerator.exportOutputFile(result);
        outputGenerator.exportXMLFile(result);
        outputGenerator.exportHighlights(result);

        if(ctx.getModeActionSelector().shouldGenerateAnalytics())
            outputGenerator.exportAnalyticsPost(result);
    }

    public static ExecutionFlow asSteps(List<ExecutionStep> plan, Context ctx, ResultBuilder result) {
        return new ExecutionFlow(plan, ctx, result);
    }

    public static ExecutionFlow justTests(Context ctx, ResultBuilder result) {
        return new ExecutionFlow(
                Arrays.asList(new RunJUnitTestsStep()),
                ctx,
                result
        );
    }

    public static ExecutionFlow justBasic(Context ctx, ResultBuilder result) {
        return new ExecutionFlow(Collections.emptyList(), ctx, result);
    }

    private List<ExecutionStep> basicSteps() {
        return Arrays.asList(new OrganizeSourceCodeStep(), new CompilationStep(), new ReplaceClassloaderStep(), new GetRunConfigurationStep());
    }


}
