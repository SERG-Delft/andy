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
            put("coverage", 0.2f);
            put("mutation", 0.3f);
            put("meta", 0.5f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 8;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement("does not check for null", 38, 40,
                        ""
                ),
                MetaTest.withLineReplacement("does not work when start index is set to a negative value", 41, 43,
                        ""
                ),
                MetaTest.insertAt("does not work with empty array", 41,
                        "if (array.length == 0) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.withLineReplacement("starts from index 1 when start index is set to a negative value", 41, 43,
                        """
                        if (startIndex < 0) {
                            startIndex = 1;
                        }
                        """
                ),
                MetaTest.withLineReplacement("skips first element", 41, 43,
                        """
                        if (startIndex < 1) {
                            startIndex = 1;
                        }
                        """
                ),
                MetaTest.withStringReplacement("always starts at index 0",
                        "int i = startIndex;",
                        "int i = 0;"
                ),
                MetaTest.withStringReplacement("skips last element",
                        "i < array.length;",
                        "i < array.length - 1;"
                ),
                MetaTest.withStringReplacement("starts searching from the back (returns last index is found multiple times)",
                        "for (int i = startIndex; i < array.length; i++)",
                        "for (int i = array.length - 1; i >= startIndex; i--)"
                ),
                MetaTest.withStringReplacement("searches for indices instead of elements",
                        "if (valueToFind == array[i])",
                        "if (valueToFind == i)"
                ),
                MetaTest.withLineReplacement("never finds anything", 44, 48,
                        ""
                ),
                MetaTest.withStringReplacement("always finds the first value",
                        "if (valueToFind == array[i])",
                        "if (true)"
                )
        );
    }

}