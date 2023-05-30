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
            put("coverage", 0.1f); 
            put("mutation", 0.3f);
            put("meta", 0.6f);
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.DelftStringUtilities");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 24;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("the string is null",
                        """
                         if (str == null) {
                            return null;
                         }
                         """,
                        ""),
                MetaTest.withStringReplacement("repeat is lower than 0",
                        """
                        if (repeat <= 0) {
                            return EMPTY;
                        }
                         """,
                        """
                        if (repeat <= 0) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("repeat is PAD_LIMIT", 56,
                        """
                        if (repeat == 8192) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("repeat is higher than PAD_LIMIT", 56,
                        """
                        if (repeat > 8192) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string is empty", 56,
                        """
                        if (str.isEmpty()) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.withStringReplacement("repeats the string repeat - 1 times",
                        "i < repeat",
                        "i < repeat - 1"),
                MetaTest.withStringReplacement("does not work correctly if the string is 1 character",
                        "return repeat(str.charAt(0), repeat);",
                        "return EMPTY;"),
                MetaTest.withLineReplacement("does not work correctly if the string is 2 characters",
                        74, 81, "return EMPTY;"),
                MetaTest.withLineReplacement("does not work correctly if the string length is higher than 2",
                        83, 87, "return EMPTY;"),
                MetaTest.withStringReplacement("negates third if-statement",
                        "repeat == 1 || inputLength == 0",
                        "repeat != 1 && inputLength != 0"),
                MetaTest.withStringReplacement("negates fourth if-statement",
                        "inputLength == 1 && repeat <= PAD_LIMIT",
                        "inputLength != 1 || repeat > PAD_LIMIT"),
                MetaTest.insertAt("the string length is 1 and repeat is higher than or equal to 3", 56,
                        """
                        if (str.length() == 1 && repeat >= 3) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string length is 2 " +
                                "and repeat is higher than or equal to 3", 56,
                        """
                        if (str.length() == 2 && repeat >= 3) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string length is higher than or equal to 3 " +
                                "and repeat is higher than or equal to 3", 56,
                        """
                        if (str.length() >= 3 && repeat >= 3) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string length is 1 and repeat is 1", 56,
                        """
                        if (str.length() == 1 && repeat == 1) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string length is 2 and repeat is 1", 56,
                        """
                        if (str.length() == 2 && repeat == 1) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """),
                MetaTest.insertAt("the string length is higher than or equal to 3 " +
                                "and repeat is 1", 56,
                        """
                        if (str.length() >= 3 && repeat == 1) {
                            throw new RuntimeException("killed the mutant");
                        }       
                        """)
        );
    }

}

