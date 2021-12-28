package nl.tudelft.cse1110.andy.execution.metatest;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.execution.metatest.evaluators.InsertAtEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.evaluators.LineReplacementEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.evaluators.MetaEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.evaluators.StringReplacementEvaluator;
import nl.tudelft.cse1110.andy.execution.metatest.implementations.LibraryMetaTest;

public abstract class AbstractMetaTest implements MetaTest {

    private final int weight;
    private final String name;

    protected AbstractMetaTest(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public static AbstractMetaTest withStringReplacement(int weight, String name, String old, String replacement) {
        MetaEvaluator evaluator = new StringReplacementEvaluator(old, replacement);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public static AbstractMetaTest withStringReplacement(String name, String old, String replacement) {
        return withStringReplacement(1, name, old, replacement);
    }

    public static AbstractMetaTest withLineReplacement(int weight, String name, int start, int end, String replacement) {
        MetaEvaluator evaluator = new LineReplacementEvaluator(start, end, replacement);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public static AbstractMetaTest withLineReplacement(String name, int start, int end, String replacement) {
        return withLineReplacement(1, name, start, end, replacement);
    }

    public static AbstractMetaTest insertAt(int weight, String name, int lineToInsertStartingIn1, String contentToAdd) {
        MetaEvaluator evaluator = new InsertAtEvaluator(lineToInsertStartingIn1, contentToAdd);
        return new LibraryMetaTest(weight, name, evaluator);
    }

    public static AbstractMetaTest insertAt(String name, int lineToInsertStartingIn1, String contentToAdd) {
        return insertAt(1, name, lineToInsertStartingIn1, contentToAdd);
    }

    @Override
    public String getNameAndWeight() {
        return String.format("%s (weight: %d)", name, weight);
    }

}
