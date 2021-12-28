package nl.tudelft.cse1110.andy.execution.metatest.implementations;

import nl.tudelft.cse1110.andy.execution.metatest.MetaTest;
import nl.tudelft.cse1110.andy.execution.metatest.evaluators.MetaEvaluator;

public class LibraryMetaTest extends MetaTest {
    private MetaEvaluator evaluator;

    public LibraryMetaTest(int weight, String name, MetaEvaluator evaluator) {
        super(weight, name);
        this.evaluator = evaluator;
    }

    public String evaluate(String oldLibraryCode) {
        return this.evaluator.evaluate(oldLibraryCode);
    }
}
