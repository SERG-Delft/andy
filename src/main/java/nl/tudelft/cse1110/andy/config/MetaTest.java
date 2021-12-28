package nl.tudelft.cse1110.andy.config;

public interface MetaTest {
    String getName();

    int getWeight();

    String getNameAndWeight();

    static MetaTest withStringReplacement(int weight, String name, String old, String replacement) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .withStringReplacement(weight, name, old, replacement);
    }

    static MetaTest withStringReplacement(String name, String old, String replacement) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .withStringReplacement(name, old, replacement);
    }

    static MetaTest withLineReplacement(int weight, String name, int start, int end, String replacement) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .withLineReplacement(weight, name, start, end, replacement);
    }

    static MetaTest withLineReplacement(String name, int start, int end, String replacement) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .withLineReplacement(name, start, end, replacement);
    }

    static MetaTest insertAt(int weight, String name, int lineToInsertStartingIn1, String contentToAdd) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .insertAt(weight, name, lineToInsertStartingIn1, contentToAdd);
    }

    static MetaTest insertAt(String name, int lineToInsertStartingIn1, String contentToAdd) {
        return nl.tudelft.cse1110.andy.execution.metatest.MetaTest
                .insertAt(name, lineToInsertStartingIn1, contentToAdd);
    }
}
