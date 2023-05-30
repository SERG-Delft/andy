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
            put("mutation", 0.35f);
            put("meta", 0.45f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.CollectionUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 5;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("does not work when coll1 is empty", 25,
                        "if (coll1 != null && coll1.isEmpty()) return true;"),
                MetaTest.insertAt("does not work when coll2 is empty", 25,
                        "if (coll2 != null && coll2.isEmpty()) return true;"),
                MetaTest.insertAt("does not work when both coll1 and coll2 are empty", 25,
                        "if (coll1 != null && coll1.isEmpty() && coll2 != null && coll2.isEmpty()) return true;"),
                MetaTest.withStringReplacement("does not work when there are no intersections",
                        "return false;",
                        "return true;"),
                MetaTest.withLineReplacement("does not work when there are multiple intersections", 25, 38,
                        """
                        int numIntersections = 0;
                        if (coll1.size() < coll2.size()) {
                            for (Object aColl1 : coll1) {
                                if (coll2.contains(aColl1)) {
                                    numIntersections++;
                                }
                            }
                        } else {
                            for (final Object aColl2 : coll2) {
                                if (coll1.contains(aColl2)) {
                                    numIntersections++;
                                }
                            }
                        }
                        return numIntersections == 1;
                        """),
                MetaTest.withLineReplacement("does not work when there is only a single intersection", 25, 38,
                        """
                        int numIntersections = 0;
                        if (coll1.size() < coll2.size()) {
                            for (Object aColl1 : coll1) {
                                if (coll2.contains(aColl1)) {
                                    numIntersections++;
                                }
                            }
                        } else {
                            for (final Object aColl2 : coll2) {
                                if (coll1.contains(aColl2)) {
                                    numIntersections++;
                                }
                            }
                        }
                        return numIntersections > 1;
                        """),
                MetaTest.withLineReplacement("only checks elements at the same position", 25, 37,
                        """
                        var it1 = coll1.iterator();
                        var it2 = coll2.iterator();
                        while (it1.hasNext() && it2.hasNext()) {
                            Object aColl1 = it1.next();
                            Object aColl2 = it2.next();
                            if (aColl1.equals(aColl2)) {
                                return true;
                            }
                        }
                        """),
                MetaTest.insertAt("does not work with collections of different sizes", 25,
                        "if (coll1.size() != coll2.size()) return false;")
        );
    }

}