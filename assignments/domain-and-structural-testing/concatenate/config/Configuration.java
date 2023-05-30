package delft;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration extends RunConfiguration {

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.15f); // was 1.0
            put("mutation", 0.45f); // was 0.0
            put("meta", 0.4f); // was 0.0
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.MathArrays");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("does not work with no arrays provided", 23,
                        "if (x.length == 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not work with a single array", 23,
                        "if (x.length == 1) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.withStringReplacement("does not work with more than 2 arrays",
                        "i < x.length;",
                        "i < x.length && i < 2;"),
                MetaTest.insertAt("does not work with an empty array provided", 32,
                        "if (curLength == 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.withStringReplacement("does not work with arrays of different sizes",
                        "curLength = x[i].length;",
                        "curLength = x[0].length;")
        );
    }

}