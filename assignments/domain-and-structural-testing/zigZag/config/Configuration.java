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
            put("coverage", 0.33f);
            put("mutation", 0.33f);
            put("meta", 0.34f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ZigZag");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 24;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("single row",
                        "return s;",
                        "throw new RuntimeException(\"killed mutant\");"),
                MetaTest.withLineReplacement("string length equals number of rows", 17, 17,
                        """
                        if(s.length() == numRows)
                            throw new RuntimeException("killed the mutant");
                        """),
                MetaTest.withStringReplacement("going down inverted",
                        "boolean goingDown = false;",
                        "boolean goingDown = true;")
        );
    }

}