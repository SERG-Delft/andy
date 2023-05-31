package delft;

import nl.tudelft.cse1110.andy.codechecker.checks.*;
import nl.tudelft.cse1110.andy.codechecker.engine.*;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends RunConfiguration {

    @Override
    public Mode mode() {
        return Mode.PRACTICE;
    }

    @Override
    public Map<String, Float> weights() {
        return new HashMap<>() {{
            put("coverage", 0.15f);
            put("mutation", 0.25f);
            put("meta", 0.4f);
            put("codechecks", 0.2f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.Summer");
    }

    @Override
    public int numberOfMutationsToConsider() {
        // overridden as one of the mutants changes
        // the floating-point comparison boundary (replaces >=0.75f with >0.75f)
        return 6;
    }

    @Override
    public CheckScript checkScript() {
        return new CheckScript(List.of(
                new SingleCheck(1, "should have at least one property",
                        new JQWikProperty(Comparison.GTE, 1)
                ),
                new SingleCheck(2, "no filtering in property methods", true,
                        new MethodCalledInProvideMethod("filter")
                ),
                new OrCheck(2, "shuffle the combined list",
                        List.of(
                                new SingleCheck(new MethodCalledInProvideMethod("shuffle")),
                                new SingleCheck(new MethodCalledInTestMethod("shuffle"))
                        )
                ),
                new AndCheck(2, "does not use Arbitraries.of(), List.of(), etc.",
                        List.of(
                                new SingleCheck(true, new MethodCalledInProvideMethod("of")),
                                new SingleCheck(true, new MethodCalledInTestMethod("of"))
                        )
                )
        ));
    }


    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement(2, "does not check on point correctly",
                        "if (temp >= 20)",
                        "if (temp > 20)"
                ),
                MetaTest.withStringReplacement("incorrect proportion",
                        "0.75f",
                        "0.7f"
                ),
                MetaTest.withStringReplacement(2, "incorrect proportion (stricter)",
                        "0.75f",
                        "0.76f"
                ),
                MetaTest.withStringReplacement("does not work with numbers greater than 15000000",
                        "if (temp >= 20)",
                        "if (temp >= 20 && temp <= 15000000)"
                ),
                MetaTest.withStringReplacement("does not work with numbers smaller than -15000000",
                        "if (temp >= 20)",
                        "if (temp >= 20 || temp < -15000000)"
                )
        );
    }

    // This class checks if certain methods have been used in the "@Provide" methods
    static class MethodCalledInProvideMethod extends MethodCalledInTestMethod {

        public MethodCalledInProvideMethod(String methodToBeCalled) {
            super(methodToBeCalled);
        }

        @Override
        protected Set<String> annotations() {
            return Set.of("Provide");
        }
    }

}
