package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.SingleCheck;
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
            put("mutation", 0.1f);
            put("meta", 0.5f);
            put("codechecks", 0.3f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.Palindrome");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 10;
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(1, "at least one property",
                        new JQWikProperty(Comparison.GTE, 1)),
                new SingleCheck(1, "at least two properties",
                        new JQWikProperty(Comparison.GTE, 2)),
                new SingleCheck(2, "at least three properties",
                        new JQWikProperty(Comparison.GTE, 3)),
                new SingleCheck(3, "four properties",
                        new JQWikProperty(Comparison.GTE, 4)),
                new SingleCheck(1, "tests should have assertions",
                        new TestMethodsHaveAssertions()),
                new SingleCheck(2, "no inputs as strings", true,
                        new UseOfStringLiterals(2)),
                new SingleCheck(2, "no loops in tests", true,
                        new LoopInTestMethods())
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement(1, "always returns false", 15, 21,
                        "return false;"),
                MetaTest.withLineReplacement(1, "always returns true", 15, 21,
                        "return true;"),
                MetaTest.withStringReplacement(2, "does not check case insensitivity",
                        ".equalsIgnoreCase(",
                        ".equals("),
                MetaTest.withLineReplacement(2, "only works for even length strings", 15, 21,
                        """
                        if (word.length() <= 1) {
                            return true;
                        }
                                                
                        String left = word.substring(0, word.length() / 2);
                        String right = word.substring(word.length() / 2);
                        StringBuilder rightSb = new StringBuilder(right);
                        rightSb.reverse();
                                                
                        return left.equalsIgnoreCase(rightSb.toString());
                        """),
                MetaTest.withLineReplacement(6, "tested only on small inputs", 21, 21,
                        """
                        /**
                         * generators like
                         * Arbitraries.strings().filter(s -> s.toLowerCase().equals(reverse(s).toLowerCase()))
                         * will most likely generate only very small inputs, e.g., aa, bb.
                         * This mutant captures whether the generator was written in a smarter way.
                         */
                        if(word.length()>10 && word.chars().distinct().count()>3)
                            throw new RuntimeException("killed the mutant");
                        return true;
                        """)
        );
    }

}