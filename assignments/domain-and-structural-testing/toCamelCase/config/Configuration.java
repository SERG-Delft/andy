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
            put("coverage", 0.3f); // was 1.0
            put("mutation", 0.3f); // was 0.0
            put("meta", 0.4f); // was 0.0
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.DelftCaseUtilities");
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not check for null",
                        "if (str == null || str.isEmpty())",
                        "if (str.isEmpty())"),

                MetaTest.insertAt("does not work with empty string", 35,
                        "if (str != null && str.isEmpty()) " +
                                "throw new RuntimeException(\"killed the mutant\");"),

                MetaTest.withStringReplacement("does not split by a space unless explicitly specified",
                        "delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));",
                        ""),

                MetaTest.withLineReplacement("only splits by space", 87, 89,
                        ""),

                MetaTest.withLineReplacement("does not split by a space if other delimiters are specified",
                        83, 86,
                        """
                        if (delimiters == null || delimiters.length == 0) {
                            delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
                            return delimiterHashSet;
                        }
                        """),

                MetaTest.withStringReplacement("does not work with null delimiters",
                        "if (delimiters == null || delimiters.length == 0) {",
                        "if (delimiters.length == 0) {"),

                MetaTest.withLineReplacement("does not work with multiple delimiters specified", 87, 89,
                        "delimiterHashSet.add(Character.codePointAt(delimiters, 0));"),

                MetaTest.withStringReplacement("does not convert string to lowercase",
                        "str = str.toLowerCase();",
                        ""),

                MetaTest.withLineReplacement("always capitalizes first letter", 43, 46,
                        "boolean capitalizeNext = true;"),

                MetaTest.withLineReplacement("never capitalizes first letter", 43, 46,
                        "boolean capitalizeNext = false;"),

                // this can occur if a mutant tries to capitalize the first letter after generating an empty output string
                MetaTest.insertAt("throws an exception if string is empty and first letter is capitalised", 35,
                        "if (str != null && str.isEmpty() && capitalizeFirstLetter) " +
                                "throw new RuntimeException(\"killed the mutant\");"),

                MetaTest.insertAt("does not work with nonexistent delimiters", 38,
                        """
                        if (delimiters != null)
                            for (char c : delimiters)
                                if (!str.contains(Character.toString(c)))
                                    throw new RuntimeException("killed the mutant");
                        """),

                MetaTest.insertAt("does not work when word contains only delimiters", 39,
                        """
                        boolean onlyDelimiters = true;
                        for (char c : str.toCharArray())
                            if (!(new String(delimiters).contains(Character.toString(c)))){
                                onlyDelimiters = false;
                                break;
                            }
                        if (onlyDelimiters) throw new RuntimeException("killed the mutant");
                        """),

                MetaTest.insertAt("does not work when word contains more than one delimiter", 39,
                        """
                        int numDelimiters = 0;
                        if (delimiters != null)
                            for (char c : delimiters)
                                if (str.contains(Character.toString(c)))
                                    numDelimiters++;
                        if (numDelimiters > 1) throw new RuntimeException("killed the mutant");
                        """)
        );
    }

}