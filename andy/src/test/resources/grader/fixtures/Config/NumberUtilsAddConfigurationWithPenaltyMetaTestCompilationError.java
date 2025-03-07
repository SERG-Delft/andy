package delft;

import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.1f);
            put("mutation", 0.3f);
            put("meta", 0.6f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.NumberUtils");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
            MetaTest.withLineReplacement("DoesNotCheckNumbersOutOfRange", 52, 53, "")
        );
    }

    @Override
    public List<MetaTest> penaltyMetaTests() {
        return List.of(
                MetaTest.withStringReplacement(1, "BadMetaTest",
                        "Collections.reverse(reversedLeft);",
                        "Collections.reverse(reversedLeft)"
                )
        );
    }
}