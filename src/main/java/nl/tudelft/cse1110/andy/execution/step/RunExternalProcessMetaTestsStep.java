package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DefaultRunConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.metatest.externalprocess.ExternalProcessMetaTest;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.MetaTestResult;
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

                boolean passesTheMetaTest = metaTest.execute(ctx, null, runCfg);

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
