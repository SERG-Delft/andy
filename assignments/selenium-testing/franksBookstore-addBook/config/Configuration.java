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
                MetaTest.withStringReplacement("Bookname cannot be empty",
                        "/BookStore/index.html",
                        "/config/metatest-1/index.html"),
                MetaTest.withStringReplacement("Saving without an author",
                        "/BookStore/index.html",
                        "/config/metatest-2/index.html"),
                MetaTest.withStringReplacement("Duplicate books should not be allowed",
                        "/BookStore/index.html",
                        "/config/metatest-3/index.html"),
                MetaTest.withStringReplacement("Book is not actually saved",
                        "/BookStore/index.html",
                        "/config/metatest-4/index.html"),
                MetaTest.withStringReplacement("Success alert is not shown",
                        "/BookStore/index.html",
                        "/config/metatest-5/index.html"),
                MetaTest.withStringReplacement("Each author can only have one book",
                        "/BookStore/index.html",
                        "/config/metatest-6/index.html")
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
