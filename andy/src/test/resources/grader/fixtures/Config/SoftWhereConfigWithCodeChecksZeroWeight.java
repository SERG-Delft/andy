package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.MockClass;
import nl.tudelft.cse1110.andy.codechecker.checks.MockitoWhen;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 1f);
            put("mutation", 0f);
            put("meta", 0f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SoftWhere");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(0, "Trip Repository should be mocked", new MockClass("TripRepository")),
                new SingleCheck(0, "Trip should be mocked", new MockClass("Trip")),
                new SingleCheck(0, "getTripById should be set up", new MockitoWhen("getTripById", Comparison.GTE, 1))
        ));
    }

}