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
        return solutionLineUnderlined(line, line, message, "Error");
    }

    private static Condition<String> libraryLine(int start, int end, String message, String purpose) {
        return lineUnderlined(start, end, message, "LIBRARY", purpose);
    }

    private static Condition<String> solutionLineUnderlined(int start, int end, String message, String purpose) {
        return lineUnderlined(start, end, message, "SOLUTION", purpose);
    }

    private static Condition<String> lineUnderlined(int start, int end, String message, String file, String purpose) {
        return containsString(String.format("{\"severity\":\"%s\"," +
                                            "\"type\":\"Marker\"," +
                                            "\"file\":\"%s\"," +
                                            "\"startLineNumber\":%d," +
                                            "\"endLineNumber\":%d," +
                                            "\"message\":\"%s\"}",
                purpose, file, start, end, message));
    }

}
