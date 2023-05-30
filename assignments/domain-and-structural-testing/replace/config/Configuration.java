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
            put("coverage", 0.25f);
            put("mutation", 0.25f);
            put("meta", 0.5f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.DelftStringUtilities");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 16;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.insertAt("the text is null", 32,
                        """
                        if (text == null) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the search text is null", 32,
                        """
                        if (searchString == null) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the replacement is null", 32,
                        """
                        if (replacement == null) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the search text is empty", 32,
                        """
                        if (searchString.isEmpty()) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the replacement is empty", 32,
                        """
                        if (replacement.isEmpty()) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the maximum is 0", 32,
                        """
                        if (max == 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the maximum is negative", 32,
                        """
                        if (max < 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the maximum is higher than or equal to 64", 32,
                        """
                        if (max >= 64) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the search string is shorter than the replacement", 45,
                        """
                        if (increase > 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the search string is equal to the replacement", 45,
                        """
                        if (increase == 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("the search string is longer than the replacement", 45,
                        """
                        if (increase < 0) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("no case sensitivity", 45,
                        """
                        if (ignoreCase == true) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                MetaTest.insertAt("case sensitivity", 45,
                        """
                        if (ignoreCase == false) {
                            throw new RuntimeException("killed the mutant");
                        }
                        """),
                // MetaTest.withStringReplacement("negate first if-statement",
                //         """
		        //         if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			    //             return text;
		        //         }
                //         """,
                //         """
                //         if (!isEmpty(text) && !isEmpty(searchString) && replacement != null && max != 0) {
                //             return text;
                //         }
                //         """),
                MetaTest.withStringReplacement("negate second if-statement",
                        """
                        if (ignoreCase) {
                            searchString = searchString.toLowerCase();
                        }
                        """,
                        """
                        if (!ignoreCase) {
                            searchString = searchString.toLowerCase();
                        }
                        """),
                MetaTest.withStringReplacement("append the search string instead of the replacement",
                        """
                        buf.append(text, start, end).append(replacement);
                        """,
                        """
                        buf.append(text, start, end).append(searchString);
                        """),
                MetaTest.withStringReplacement("negate if-statement in loop and break after one iteration",
                        """
                        if (--max == 0) {
                            break;
                        }
                        """,
                        """
                        if (--max != 0) {
                            break;
                        }
                        """),
                MetaTest.withStringReplacement("negate fourth if-statement",
                        """
                        increase = increase < 0 ? 0 : increase;
                        """,
                        """
                        increase = increase >= 0 ? 0 : increase;
                        """),
                MetaTest.withStringReplacement("does not work for case sensitivity",
                        """
                        end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
                        """,
                        """
                        end = indexOfIgnoreCase(text, searchString, start);
                        """),
                MetaTest.withStringReplacement("does not work for case insensitivity",
                        """
                        end = ignoreCase ? indexOfIgnoreCase(text, searchString, start) : indexOf(text, searchString, start);
                        """,
                        """
                        end = indexOf(text, searchString, start);
                        """)
        );
    }

}
