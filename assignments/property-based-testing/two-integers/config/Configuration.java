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
            put("coverage", 0.25f); // was 1.0
            put("mutation", 0.5f); // was 0.0
            put("meta", 0.25f); // was 0.0
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.TwoIntegers");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not check n < 1 ",
                        "if (n < 1 || n > 99 || m < 1 || m > 99)",
                        "if (n > 99 || m < 1 || m > 99)"),
                MetaTest.withStringReplacement("does not check n > 99 ",
                        "if (n < 1 || n > 99 || m < 1 || m > 99)",
                        "if (n < 1 || m < 1 || m > 99)"),
                MetaTest.withStringReplacement("does not check m < 1 ",
                        "if (n < 1 || n > 99 || m < 1 || m > 99)",
                        "if (n < 1 || n > 99 || m > 99)"),
                MetaTest.withStringReplacement("does not check m > 99 ",
                        "if (n < 1 || n > 99 || m < 1 || m > 99)",
                        "if (n < 1 || n > 99 || m < 1)"),
                MetaTest.insertAt("does not check m and n both invalid", 6,
                        "if ((m < 0 || m > 99) && (n < 0 || n > 99)) throw new RuntimeException(\"killed the mutant\");")
        );
    }

}