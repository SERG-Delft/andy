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
            put("coverage", 0.25f);
            put("mutation", 0.0f);
            put("meta", 0.5f);
            put("codechecks", 0.25f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.ArrayUtils");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 8;
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(1, "at least one property",
                        new JQWikProperty(Comparison.GTE, 1)),
                new SingleCheck(1, "tests should have assertions",
                        new TestMethodsHaveAssertions()),
                new SingleCheck(1, "no loops in tests", true,
                        new LoopInTestMethods()),
                new SingleCheck(7, "no use of indexOf() methods to help", true,
                        new MethodCalledInTestMethod("indexOf"))
        )
        );
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withLineReplacement(1, "always returns not found", 38, 49,
                        "return INDEX_NOT_FOUND;"),
                MetaTest.withLineReplacement(1, "always returns start index", 38, 49,
                        "return startIndex;"),
                MetaTest.withStringReplacement(2, "does not use start index",
                        "int i = startIndex;",
                        "int i = 0;")
        );
    }

}