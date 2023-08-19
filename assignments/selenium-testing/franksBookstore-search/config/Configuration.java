package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public List<String> classesUnderTest() {
        return List.of();
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.0f);
            put("mutation", 0.0f);
            put("meta", 1.0f);
            put("codechecks", 0.0f);
        }};
    }
    
    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("search does not work at all",
                        "/BookStore/index.html",
                        "/config/metatest-1/index.html"),
                MetaTest.withStringReplacement("search requires full strings",
                        "/BookStore/index.html",
                        "/config/metatest-2/index.html"),
                MetaTest.withStringReplacement("no result does not give any information",
                        "/BookStore/index.html",
                        "/config/metatest-3/index.html"),
                MetaTest.withStringReplacement("all results are always displayed",
                        "/BookStore/index.html",
                        "/config/metatest-4/index.html")
        );
    }

    @Override
    public boolean skipJacoco() {
        return true;
    }

    @Override
    public boolean skipPitest() {
        return true;
    }
}
