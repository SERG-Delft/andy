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
            put("coverage", 0.1f); // was 1.0
            put("mutation", 0.1f);
            put("meta", 0.8f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 6;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("throws exception if array is null",
                        """
                        if (array == null) {
                            return;
                        }
                        """,
                        ""
                ),
                MetaTest.withStringReplacement("always starts at index 0",
                        "int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;",
                        "int i = 0;"
                ),
                MetaTest.withStringReplacement("does not promote negative start index",
                        "int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;",
                        "int i = startIndexInclusive;"
                ),
                MetaTest.withStringReplacement("start index is exclusive",
                        "int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;",
                        "int i = (startIndexInclusive + 1) < 0 ? 0 : (startIndexInclusive + 1);"
                ),
                MetaTest.withStringReplacement("skips index 0",
                        "int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;",
                        "int i = startIndexInclusive < 1 ? 1 : startIndexInclusive;"
                ),
                MetaTest.withStringReplacement("always ends at last index",
                        "int j = Math.min(array.length, endIndexExclusive) - 1;",
                        "int j = array.length - 1;"
                ),
                MetaTest.withStringReplacement("does not demote end index greater than the length of the array",
                        "int j = Math.min(array.length, endIndexExclusive) - 1;",
                        "int j = endIndexExclusive - 1;"
                ),
                MetaTest.withStringReplacement("end index is inclusive",
                        "int j = Math.min(array.length, endIndexExclusive) - 1;",
                        "int j = Math.min(array.length - 1, endIndexExclusive);"
                ),
                MetaTest.withStringReplacement("skips last index of array",
                        "int j = Math.min(array.length, endIndexExclusive) - 1;",
                        "int j = Math.min(array.length - 1, endIndexExclusive) - 1;"
                ),
                MetaTest.insertAt("throws exception if start index is greater than end index", 32,
                        "if (i > j) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("throws exception if range is empty", 32,
                        "if (i == j) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("throws exception with range containing a single element", 32,
                        "if (j - 1 == i) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("does not work with range with even length", 32,
                        "if (j > i && (j - i) % 2 == 1) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("does not work with range with odd length", 32,
                        "if (j > i && (j - i) % 2 == 0) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("throws exception if end index is smaller than 0", 32,
                        "if (j < 0) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("throws exception if start index is greater than or equal to the length of the array", 32,
                        "if (i >= array.length) throw new RuntimeException(\"killed the mutant\");"
                )
        );
    }

}