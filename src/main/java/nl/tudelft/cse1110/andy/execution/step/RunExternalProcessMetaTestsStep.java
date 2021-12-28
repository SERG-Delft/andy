package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.metatest.implementations.ExternalProcessMetaTest;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.grade.GradeCalculator;
import nl.tudelft.cse1110.andy.result.MetaTestResult;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;

import java.util.ArrayList;
import java.util.List;

public class RunExternalProcessMetaTestsStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        RunConfiguration runCfg = ctx.getRunConfiguration();

        int score = 0;
        int totalWeight = 0;

        try {
            List<MetaTest> metaTests = runCfg.metaTests();
            List<MetaTestResult> metaTestResults = new ArrayList<>();

            for (MetaTest abstractMetaTest : metaTests) {
                if (!(abstractMetaTest instanceof ExternalProcessMetaTest)) {
                    continue;
                }

                ExternalProcessMetaTest metaTest = (ExternalProcessMetaTest) abstractMetaTest;

                /* Start the meta external process */
                metaTest.startExternalProcess();

                /* Run the test suite using our existing JUnit runner */
                ResultBuilder metaResultBuilder = new ResultBuilder(new Context(Action.META_TEST), new GradeCalculator());

                RunJUnitTestsStep jUnitStep = new RunJUnitTestsStep();
                jUnitStep.execute(ctx, metaResultBuilder);

                /* Kill the meta external process */
                metaTest.killExternalProcess();

                /*
                 * Status and possible error messages of the meta external process are ignored.
                 * The external process may crash as part of normal operation as it is supposed to be a
                 * buggy implementation due to the nature of meta tests.
                 */

                /* Retrieve meta test result */
                Result metaResult = metaResultBuilder.build();

                /* Check the result. If there's a failing test, the test suite is good! */
                int testsRan = metaResult.getTests().getTestsRan();
                int testsSucceeded = metaResult.getTests().getTestsSucceeded();
                boolean passesTheMetaTest = testsSucceeded < testsRan;

                if (passesTheMetaTest) {
                    score += metaTest.getWeight();
                }

                metaTestResults.add(new MetaTestResult(metaTest.getName(), metaTest.getWeight(), passesTheMetaTest));

                totalWeight += metaTest.getWeight();
            }

            result.logMetaTests(score, totalWeight, metaTestResults);
        } catch (Exception ex) {
            result.genericFailure(this, ex);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunExternalProcessMetaTestsStep;
    }
}
