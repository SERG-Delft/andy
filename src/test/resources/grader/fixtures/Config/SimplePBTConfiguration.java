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
            put("coverage", 1f);
            put("mutation", 0f);
            put("meta", 0f);
            put("codechecks", 0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.SimplePBT");
    }

    @Override
    public int numberOfJQWikTries() {
        return 5;
    }
}