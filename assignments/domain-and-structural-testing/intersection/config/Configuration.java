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
        return List.of("delft.ListUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 2;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("returns empty list when list1 is null", 29,
                        "if (list1 == null) return result;"
                ),
                MetaTest.insertAt("returns empty list when list2 is null", 29,
                        "if (list2 == null) return result;"
                ),
                MetaTest.withStringReplacement("returns wrong elements",
                        "if (hashSet.contains(e))",
                        "if (!hashSet.contains(e))"
                ),
                MetaTest.insertAt("returns null when there are no elements in the intersection", 42,
                        "if (result.isEmpty()) return null;"
                ),
                MetaTest.withStringReplacement("does not work correctly when one of the lists is empty",
                        "if (hashSet.contains(e))",
                        "if (hashSet.isEmpty() || hashSet.contains(e))"
                ),
                MetaTest.withStringReplacement("adds repeated elements to result multiple times",
                        "hashSet.remove(e);",
                        ""
                ),
                MetaTest.insertAt("does not work when one of the lists contains all of the elements of the other one", 42,
                        "if (hashSet.isEmpty()) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("does not work when there is only one element in the intersection", 42,
                        "if (result.size() == 1) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("finds only the first element in the intersection", 40,
                        "break;"
                ),
                MetaTest.withLineReplacement("compares only elements in the same index", 29, 41,
                        """
                        for (int i = 0; i < list1.size() && i < list2.size(); i++) {
                            if (list1.get(i).equals(list2.get(i))) {
                                result.add(list1.get(i));
                            }
                        }
                        """
                )
        );
    }

}