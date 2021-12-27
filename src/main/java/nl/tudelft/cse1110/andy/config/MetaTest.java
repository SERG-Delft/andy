package nl.tudelft.cse1110.andy.config;

import nl.tudelft.cse1110.andy.config.metatest.evaluators.MetaEvaluator;
import nl.tudelft.cse1110.andy.config.metatest.evaluators.StringReplacementEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaTest {

    private final int weight;
    private String name;
    private MetaEvaluator evaluator;

    private MetaTest(int weight, String name, MetaEvaluator evaluator) {
        this.weight = weight;
        this.name = name;
        this.evaluator = evaluator;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return weight;
    }

    public static MetaTest withStringReplacement(int weight, String name, String old, String replacement) {
        MetaEvaluator evaluator = new StringReplacementEvaluator(old, replacement);
        return new MetaTest(weight, name, evaluator);
    }

    public static MetaTest withStringReplacement(String name, String old, String replacement) {
        return withStringReplacement(1, name, old, replacement);
    }

    public static MetaTest withLineReplacement(int weight, String name, int start, int end, String replacement) {
        MetaEvaluator evaluator = new LineReplacementEvaluator(start, end, replacement);
        return new MetaTest(weight, name, evaluator);
    }

    public static MetaTest withLineReplacement(String name, int start, int end, String replacement) {
        return withLineReplacement(1, name, start, end, replacement);
    }

    public static MetaTest insertAt(int weight, String name, int lineToInsertStartingIn1, String contentToAdd) {
        MetaEvaluator evaluator = new InsertAtEvaluator(lineToInsertStartingIn1, contentToAdd);
        return new MetaTest(weight, name, evaluator);
    }

    public static MetaTest insertAt(String name, int lineToInsertStartingIn1, String contentToAdd) {
        return insertAt(1, name, lineToInsertStartingIn1, contentToAdd);
    }

    public String evaluate(String oldLibraryCode) {
        return this.evaluator.evaluate(oldLibraryCode);
    }

    public String getNameAndWeight() {
        return String.format("%s (weight: %d)", name, weight);
    }

    private static class LineReplacementEvaluator implements MetaEvaluator {

        private int start;
        private int end;
        private String replacement;

        public LineReplacementEvaluator(int start, int end, String replacement) {
            this.start = start - 1;
            this.end = end - 1;
            this.replacement = replacement;
        }

        @Override
        public String evaluate(String oldLibraryCode) {
            List<String> lines = oldLibraryCode.lines().collect(Collectors.toList());
            List<String> result = new ArrayList<>(lines.subList(0, this.start));
            result.add(this.replacement);
            result.addAll(lines.subList(this.end + 1, lines.size()));

            String resultStr = String.join("\n", result);
            return resultStr;
        }
    }

    private static class InsertAtEvaluator implements MetaEvaluator {

        private final int lineToInsert;
        private final String contentToAdd;

        public InsertAtEvaluator(int lineToInsertStartingIn1, String contentToAdd) {
            this.lineToInsert = lineToInsertStartingIn1;
            this.contentToAdd = contentToAdd;
        }

        @Override
        public String evaluate(String oldLibraryCode) {
            List<String> lines = oldLibraryCode.lines().collect(Collectors.toList());

            // handles possible out of the boundary values
            int sanitizedLineToInsert = lineToInsert - 1;
            if(sanitizedLineToInsert<0)
                sanitizedLineToInsert = 0;
            if(sanitizedLineToInsert>lines.size())
                sanitizedLineToInsert = lines.size();

            lines.add(sanitizedLineToInsert, contentToAdd);

            return lines.stream().collect(Collectors.joining("\n"));
        }
    }
}
