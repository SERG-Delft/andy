package nl.tudelft.cse1110.grader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MetaTest {

    private String name;
    private MetaEvaluator evaluator;

    private MetaTest(String name, MetaEvaluator evaluator) {
        this.name = name;
        this.evaluator = evaluator;
    }

    public String getName() {
        return this.name;
    }

    public static MetaTest withStringReplacement(String name, String old, String replacement) {
        MetaEvaluator evaluator = new StringReplacementEvaluator(old, replacement);
        return new MetaTest(name, evaluator);
    }

    public static MetaTest withLineReplacement(String name, int start, int end, String replacement) {
        MetaEvaluator evaluator = new LineReplacementEvaluator(start, end, replacement);
        return new MetaTest(name, evaluator);
    }

    public String evaluate(String oldLibraryCode) {
        return this.evaluator.evaluate(oldLibraryCode);
    }

    private interface MetaEvaluator {
        String evaluate(String oldLibraryCode);
    }

    private static class StringReplacementEvaluator implements MetaEvaluator {

        private String old;
        private String replacement;

        public StringReplacementEvaluator(String old, String replacement) {
            this.old = old;
            this.replacement = replacement;
        }

        @Override
        public String evaluate(String oldLibraryCode) {
            String shiftedOld = this.old.replaceAll("(?m)^\\s+", "");
            String shiftedOldLibraryCode = oldLibraryCode.replaceAll("(?m)^\\s+", "");
            String replaced = shiftedOldLibraryCode.replace(shiftedOld, this.replacement);

            if (replaced.equals(oldLibraryCode)) {
                throw new RuntimeException("Meta test failed to find this text replacement:\n" + this.old);
            }

            return replaced;
        }
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
            List<String> firstPart = new ArrayList<>(lines.subList(0, this.start));
            List<String> secondPart = new ArrayList<>(lines.subList(this.end, lines.size()));

            firstPart.add(this.replacement);

            String firstPartString = firstPart.stream().collect(Collectors.joining("\n"));
            String secondPartString = secondPart.stream().collect(Collectors.joining("\n"));
            return firstPartString + secondPartString;
        }
    }
}
