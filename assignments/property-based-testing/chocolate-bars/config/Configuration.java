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
            put("coverage", 0.2f); // was 1.0
            put("mutation", 0.5f); // was 0.0
            put("meta", 0.3f); // was 0.0
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ChocolateBars");
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("does not work properly when bars are only big", 8,
                        "if (small == 0 && big > 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not work properly when bars are only small", 8,
                        "if (small > 0 && big == 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not work properly when there are 0 boxes", 8,
                        "if (total == 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not work properly when there are both big and small bars", 8,
                        "if (small > 0 && big > 0) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not work properly when there are no bars and no boxes", 8,
                        "if (small == 0 && big == 0 && total == 0) throw new RuntimeException(\"killed the mutant\");")
        );
    }

}