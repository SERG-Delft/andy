package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.AndCheck;
import nl.tudelft.cse1110.andy.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.andy.codechecker.engine.OrCheck;
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
        return List.of("delft.Triangle");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 9;
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(2, "should have property(ies)",
                        new JQWikProperty(Comparison.GTE, 1)),
                new OrCheck(8, "either make use of Arbitraries or JQWik IntRange-like annotations",
                        List.of(
                                new AndCheck(List.of(
                                        new SingleCheck(new JQWikArbitrary()),
                                        new SingleCheck(new JQWikProvide(Comparison.GTE, 1))
                                )),
                                new AndCheck(List.of(
                                        new SingleCheck(new JQWikProvide(Comparison.EQ, 0)),
                                        new SingleCheck(new JQWikProperty(Comparison.GTE, 3)),
                                        new SingleCheck(new JQWikProvideAnnotations())
                                ))
                        )
                )
        ));
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not check side a",
                        "if (a >= (b + c) || c >= (b + a) || b >= (a + c))",
                        "if (c >= (b + a) || b >= (a + c))"),
                MetaTest.withStringReplacement("does not check side b",
                        "if (a >= (b + c) || c >= (b + a) || b >= (a + c))",
                        "if (a >= (b + c) || c >= (b + a))"),
                MetaTest.withStringReplacement("does not check side c",
                        "if (a >= (b + c) || c >= (b + a) || b >= (a + c))",
                        "if (a >= (b + c) || b >= (a + c))"),
                MetaTest.withStringReplacement("always returns the wrong result",
                        """
                        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
                            return false;
                        return true;
                        """,
                        """
                        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
                            return true;
                        return false;
                        """),
                MetaTest.withStringReplacement("always returns true",
                        """
                        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
                            return false;
                        return true;
                        """,
                        "return true;"),
                MetaTest.withStringReplacement("always returns false",
                        """
                        if (a >= (b + c) || c >= (b + a) || b >= (a + c))
                            return false;
                        return true;
                        """,
                        "return false;")
        );
    }
}