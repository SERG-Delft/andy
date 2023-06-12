package integration;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Action;
import nl.tudelft.cse1110.andy.execution.step.GetRunConfigurationStep;
import nl.tudelft.cse1110.andy.result.Result;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RunConfigurationTests extends IntegrationTestBase {

    @Test
    void invalidSum() {
        HashMap<String, Float> weights = new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.1f);
            put("meta", 0.1f);
            put("codechecks", 0.1f);
        }};
        RunConfiguration config = new RunConfiguration() {
            @Override
            public List<String> classesUnderTest() {
                return null;
            }

            @Override
            public Map<String, Float> weights() {
                return weights;
            }
        };
        assertFalse(GetRunConfigurationStep.isValidRunConfiguration(config));
        weights.put("mutation", 0.8f);
        config = new RunConfiguration() {
            @Override
            public List<String> classesUnderTest() {
                return null;
            }

            @Override
            public Map<String, Float> weights() {
                return weights;
            }
        };
        assertFalse(GetRunConfigurationStep.isValidRunConfiguration(config));
    }

    @Test
    void validSum() {
        RunConfiguration config = new RunConfiguration() {
            @Override
            public List<String> classesUnderTest() {
                return null;
            }

            @Override
            public Map<String, Float> weights() {
                return new HashMap<>() {{
                    put("coverage", 0.1f);
                    put("mutation", 0.1f);
                    put("meta", 0.1f);
                    put("codechecks", 0.7f);
                }};
            }
        };
        assertTrue(GetRunConfigurationStep.isValidRunConfiguration(config));
    }

    @Test
    void skipTestsWithMocksInvalid() {
        RunConfiguration config = Mockito.mock(RunConfiguration.class);
        Mockito.when(config.weights()).thenReturn(new HashMap<>() {{
            put("coverage", 0.7f);
            put("mutation", 0.1f);
            put("meta", 0.1f);
            put("codechecks", 0.1f);
        }});
        Mockito.when(config.skipJacoco()).thenReturn(true);

        assertFalse(GetRunConfigurationStep.isValidRunConfiguration(config));

        Mockito.when(config.skipJacoco()).thenReturn(false);
        Mockito.when(config.skipPitest()).thenReturn(true);

        assertFalse(GetRunConfigurationStep.isValidRunConfiguration(config));
    }

    @Test
    void skipTestsWithMocksValid() {
        RunConfiguration config = Mockito.mock(RunConfiguration.class);
        Mockito.when(config.weights()).thenReturn(new HashMap<>() {{
            put("coverage", 0f);
            put("mutation", 0.0f);
            put("meta", 0.2f);
            put("codechecks", 0.8f);
        }});
        Mockito.when(config.skipJacoco()).thenReturn(true);

        assertTrue(GetRunConfigurationStep.isValidRunConfiguration(config));

        Mockito.when(config.skipJacoco()).thenReturn(false);
        Mockito.when(config.skipPitest()).thenReturn(true);

        assertTrue(GetRunConfigurationStep.isValidRunConfiguration(config));
    }

    @Test
    void checkExceptionHandling() {
        // How to catch exception.
        Result result = run("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationInvalid");
    }
}
