package nl.tudelft.cse1110.andy.grader.execution;

import nl.tudelft.cse1110.andy.grader.execution.step.*;
import nl.tudelft.cse1110.andy.grader.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.grader.grade.GradeValues;
import nl.tudelft.cse1110.andy.grader.grade.GradeWeight;
import nl.tudelft.cse1110.andy.grader.result.OutputGenerator;
import nl.tudelft.cse1110.andy.grader.result.ResultBuilder;

import java.util.*;

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

        calculateFinalGrade();
        generateOutput();
    }

    private void calculateFinalGrade() {

        // if there is no run configuration (i.e., compilation failed), it's a zero.
        if(ctx.getRunConfiguration() == null) {
            result.logFinalGrade(0);
        } else {
            int finalGrade = finalGrade(
                    buildGradeWeights(ctx.getRunConfiguration().weights(), ctx.getRunConfiguration().failureGivesZero()),
                    result.grades(),
                    result.isFailed());

            result.logFinalGrade(finalGrade);
        }
    }

    private GradeWeight buildGradeWeights(Map<String, Float> weights, boolean failureGivesZero) {
        float coverage = weights.get("coverage");
        float mutation = weights.get("mutation");
        float meta = weights.get("meta");
        float codechecks = weights.get("codechecks");

        return new GradeWeight(failureGivesZero, coverage, mutation, meta, codechecks);
    }

    private int finalGrade(GradeWeight weights, GradeValues grades, boolean failed) {
        return new GradeCalculator(weights).calculateFinalGrade(grades, failed);
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
