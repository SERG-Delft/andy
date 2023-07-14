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
                MetaTest.withStringReplacement("no empty field check",
                        "/BookStore/index.html",
                        "/config/metatest-1/index.html"),
                MetaTest.withStringReplacement("no duplicate author check",
                        "/BookStore/index.html",
                        "/config/metatest-2/index.html"),
                MetaTest.withStringReplacement("does not add to database",
                        "/BookStore/index.html",
                        "/config/metatest-3/index.html")
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
