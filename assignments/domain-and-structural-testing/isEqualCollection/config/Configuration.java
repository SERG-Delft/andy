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
            put("mutation", 0.15f);
            put("meta", 0.75f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.CollectionUtils", "delft.CollectionUtils.CardinalityHelper");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 9;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("does not work when list A is empty", 114,
                        "if (a.isEmpty()) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("does not work when list B is empty", 114,
                        "if (b.isEmpty()) throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.withLineReplacement("returns true when sizes are not equal", 115, 115,
                        "return true;"
                ),
                MetaTest.withLineReplacement("does not work when there are multiple occurrences of an element", 91, 91,
                        "throw new RuntimeException(\"killed the mutant\");"
                ),
                MetaTest.insertAt("only considers first element when building cardinality map", 93,
                        "break;"
                ),
                MetaTest.withLineReplacement("does not compare list and cardinality sizes", 114, 120,
                        "final CardinalityHelper<Object> helper = new CardinalityHelper<>(a, b);"
                ),
                MetaTest.withLineReplacement("throws NullPointerException when list A contains elements not in list B", 62, 65,
                        "return count.intValue();"
                ),
                MetaTest.withStringReplacement("only checks whether elements exist but not their cardinalities",
                        "return count.intValue();",
                        "return 1;"
                ),
                MetaTest.withLineReplacement("does not check cardinalities but only whether the lists are equal", 117, 125,
                        """
                        ArrayList<?> listA = new ArrayList<>(a);
                        ArrayList<?> listB = new ArrayList<>(b);
                        for(int i = 0; i < listA.size(); i++){
                            if(!listA.get(i).equals(listB.get(i))){
                                return false;
                            }
                        }
                        """
                ),
                MetaTest.withStringReplacement("compares first list to itself",
                        "return getFreq(obj, cardinalityB);",
                        "return getFreq(obj, cardinalityA);"
                )
        );
    }

}