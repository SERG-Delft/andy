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
            put("mutation", 0.4f); // was 0.0
            put("meta", 0.4f); // was 0.0
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
                MetaTest.withStringReplacement("does not check for null str",
                        "if (str == null || isEmpty(open) || isEmpty(close)) {",
                        "if (isEmpty(open) || isEmpty(close)) {"),

                MetaTest.withStringReplacement("does not check for null open",
                        "if (str == null || isEmpty(open) || isEmpty(close)) {",
                        "if (str == null || open.isEmpty() || isEmpty(close)) {"),

                MetaTest.withStringReplacement("does not check for empty open",
                        "if (str == null || isEmpty(open) || isEmpty(close)) {",
                        "if (str == null || open == null || isEmpty(close)) {"),

                MetaTest.withStringReplacement("does not check for null close",
                        "if (str == null || isEmpty(open) || isEmpty(close)) {",
                        "if (str == null || isEmpty(open) || close.isEmpty()) {"),

                MetaTest.withStringReplacement("does not check for empty close",
                        "if (str == null || isEmpty(open) || isEmpty(close)) {",
                        "if (str == null || isEmpty(open) || close == null) {"),

                MetaTest.withLineReplacement("returns null with empty str", 40, 40,
                        "return null;"),

                MetaTest.withStringReplacement("skips first character",
                        "int pos = 0;",
                        "int pos = 1;"),

                MetaTest.withLineReplacement("returns empty list when no there are no matches",
                        59, 61, ""),

                MetaTest.withLineReplacement("only returns first match", 62, 62,
                        "return new String[] { list.get(0) };"),

                MetaTest.insertAt("does not work when string contains open and close next to each other", 42,
                        "if (str.contains(open + close)) throw new RuntimeException(\"killed the mutant\");"),

                MetaTest.withStringReplacement("skips a character after finding a match",
                        "pos = end + closeLen;",
                        "pos = end + closeLen + 1;"),

                MetaTest.withLineReplacement("does not work with open string longer than 1 character", 34, 37,
                        """
                        public static String[] substringsBetween(final String str, String open, final String close) {
                            if (str == null || isEmpty(open) || isEmpty(close)) {
                                return null;
                            }
                            open = open.substring(1, open.length());
                        """),

                MetaTest.withLineReplacement("does not work with close string longer than 1 character", 34, 37,
                        """
                        public static String[] substringsBetween(final String str, final String open, String close) {
                            if (str == null || isEmpty(open) || isEmpty(close)) {
                                return null;
                            }
                            close = close.substring(0, open.length() - 1);
                        """)
        );
    }

}