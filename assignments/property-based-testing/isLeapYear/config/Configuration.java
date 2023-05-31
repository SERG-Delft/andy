package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.Comparison;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikArbitraries;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikProperty;
import nl.tudelft.cse1110.andy.codechecker.checks.JQWikProvide;
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
            put("coverage", 0.2f);
            put("mutation", 0.1f); // was 0.0
            put("meta", 0.4f); // was 0.0
            put("codechecks", 0.3f); // was 0.8
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.LeapYear");
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(1, "should have at least 2 properties",
                        new JQWikProperty(Comparison.GTE, 2)
                ),
                new SingleCheck(1, "should have at least 2 provides",
                        new JQWikProvide(Comparison.GTE, 2)
                ),
                new SingleCheck(8, "should make use of Arbitraries.integers()",
                        new JQWikArbitraries("integers")
                )
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not check if year is divisible by 400",
                        """
                        if (year % 400 == 0)
                            return true;
                        """,
                        ""),
                MetaTest.withStringReplacement("does not check if year is divisible by 100",
                        """
                        if (year % 100 == 0)
                            return false;
                        """,
                        ""),
                MetaTest.withStringReplacement("returns false if year is divisible by 400",
                        """
                        if (year % 400 == 0)
                            return true;
                        """,
                        """
                        if (year % 400 == 0)
                            return false;
                        """),
                MetaTest.withStringReplacement("returns true if year is divisible by 100",
                        """
                        if (year % 100 == 0)
                            return false;
                        """,
                        """
                        if (year % 100 == 0)
                            return true;
                        """),
                MetaTest.withStringReplacement("returns false if year is divisible by 4",
                        "return year % 4 == 0;",
                        "return year % 4 != 0;"),
                MetaTest.withStringReplacement("always returns false if year is divisible by neither 400 nor 100",
                        "return year % 4 == 0;",
                        "return false;"),
                MetaTest.withStringReplacement("always returns true if year is divisible by neither 400 nor 100",
                        "return year % 4 == 0;",
                        "return true;")
        );
    }

}