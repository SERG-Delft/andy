package unit.writer.weblab;

import org.assertj.core.api.Condition;

import static unit.writer.standard.StandardResultTestAssertions.containsString;

public class WebLabEditorFeedbackJsonTestAssertions {

    public static Condition<String> editorFeedbackFullyCovered(int start, int end) {
        return libraryLine(start, end, "100% coverage", "Info");
    }

    public static Condition<String> editorFeedbackPartiallyCovered(int start, int end) {
        return libraryLine(start, end, "Partial coverage", "Hint");
    }

    public static Condition<String> editorFeedbackNotCovered(int start, int end) {
        return libraryLine(start, end, "No coverage", "Warning");
    }

    public static Condition<String> editorFeedbackCompilationError(int line, String message) {
        return solutionLine(line, line, message, "Error");
    }

    private static Condition<String> libraryLine(int start, int end, String message, String purpose) {
        return line(start, end, message, "LIBRARY", purpose);
    }

    private static Condition<String> solutionLine(int start, int end, String message, String purpose) {
        return line(start, end, message, "SOLUTION", purpose);
    }

    private static Condition<String> line(int start, int end, String message, String location, String purpose) {
        return containsString(String.format("{\"location\":\"%s\"," +
                                            "\"startLine\":%d," +
                                            "\"endLine\":%d," +
                                            "\"severity\":\"%s\"," +
                                            "\"message\":\"%s\"}",
                location, start, end, purpose, message));
    }

}
