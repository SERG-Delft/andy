package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.MockClass;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck("Dummy check that always passes", true, new MockClass("Something"))
        ));
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.0f);
            put("mutation", 0.1f);
            put("meta", 0.0f);
            put("codechecks", 0.9f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of();
    }

    @Override
    public Mode mode() {
        return Mode.GRADING;
    }
}