package integration;

import nl.tudelft.cse1110.andy.execution.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionFlow;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.result.Result;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class GenericFailureTest extends IntegrationTestBase {

    private ExecutionStep badStep;

    @Override
    protected void addSteps(ExecutionFlow flow) {
        badStep = mock(ExecutionStep.class);

        doThrow(new RuntimeException("Some super error here"))
                .when(badStep).execute(any(Context.class), any(ResultBuilder.class));

        flow.addSteps(Arrays.asList(badStep));
    }

    @Test
    void genericFailureHasPrecedence() {
        Result result = run(Action.TESTS, "NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass");

        assertThat(result.hasGenericFailure()).isTrue();
        assertThat(result.getGenericFailure()).isNull();
        assertThat(result.getGenericFailureStep())
                .isSameAs(badStep);
        assertThat(result.getGenericFailureException())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Some super error here");
    }

}
