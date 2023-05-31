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
            put("coverage", 0.3f); // was 0.5
            put("mutation", 0.35f); // was 0.5
            put("meta", 0.35f); // was 0.0
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
                MetaTest.withStringReplacement("does not remove duplicates",
                        "TreeSet",
                        "PriorityQueue"),
                MetaTest.withStringReplacement("skips first element",
                        "for (int i = 0; i < data.length; i++)",
                        "for (int i = 1; i < data.length; i++)"),
                MetaTest.withStringReplacement("skips last element",
                        "for (int i = 0; i < data.length; i++)",
                        "for (int i = 0; i < data.length-1; i++)"),
                MetaTest.withStringReplacement("output array is larger than the number of elements",
                        "final int count = values.size();",
                        "final int count = values.size() + 1;")
//                MetaTest.insertAt("does not handle null properly", 27,
//                        """
//                        if (data == null) throw new RuntimeException("killed the mutant");
//                        """),
//                MetaTest.insertAt("does not handle zero elements properly", 27,
//                        """
//                        if (data != null && data.length == 0) throw new RuntimeException("killed the mutant");
//                        """),
//                MetaTest.insertAt("does not handle large inputs properly", 27,
//                        """
//                        if (data != null && data.length > 50) throw new RuntimeException("killed the mutant");
//                        """)
        );
    }

}