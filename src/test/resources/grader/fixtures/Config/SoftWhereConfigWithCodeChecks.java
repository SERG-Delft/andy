package domain.addingnumbers;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.codechecker.engine.SingleCheck;
import nl.tudelft.cse1110.grader.config.RunConfiguration;
import nl.tudelft.cse1110.grader.config.MetaTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0f);
            put("mutation", 0f);
            put("meta", 0f);
            put("codechecks", 1f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SoftWhere");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
            new SingleCheck(1, "MockClass", "Trip Repository should be mocked", false, "TripRepository"),
            new SingleCheck(1, "MockClass", "Trip should not be mocked", true, "Trip"),
            new SingleCheck(1, "MockitoWhen", "getTripById should be set up", false, "getTripById GTE 1")
        ));
    }

}