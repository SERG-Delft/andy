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
            put("mutation", 0.5f); // was 0.0
            put("meta", 0.2f); // was 0.0
            put("codechecks", 0.0f);
        }};
    }

    @Override
    public List<String> classesUnderTest() {
        return List.of("delft.TaxIncome");
    }

    @Override
    public int numberOfMutationsToConsider() {
        return 33;
    }

    @Override
    public List<MetaTest> metaTests() {
        return List.of(
                MetaTest.withStringReplacement("does not handle values smaller than 0",
                        "if (0 <= income && ",
                        "if ("),
                MetaTest.insertAt("does not handle fractions", 8,
                        "if ((int) income != income) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.insertAt("does not handle very large values", 8,
                        "if (income > 100000000) throw new RuntimeException(\"killed the mutant\");"),
                MetaTest.withStringReplacement("returns 0 instead of CANNOT_CALC_TAX",
                        "return CANNOT_CALC_TAX;",
                        "return 0;")
        );
    }

}