package utils;

import org.assertj.core.api.Condition;
import unit.writer.standard.StandardResultTestAssertions;

public class WebLabEditorFeedbackJsonTestAssertions {

    public static Condition<String> editorFeedbackFullyCovered(int start, int end) {
        return libraryLineHighlighted(start, end, "100% coverage", "blue");
    }

    public static Condition<String> editorFeedbackPartiallyCovered(int start, int end) {
        return libraryLineHighlighted(start, end, "Partial coverage", "yellow");
    }

    public static Condition<String> editorFeedbackNotCovered(int start, int end) {
        return libraryLineHighlighted(start, end, "No coverage", "red");
    }

    public static Condition<String> editorFeedbackCompilationError(int line, String message) {
        return solutionLineUnderlined(line, line, message, "Error");
    }

    private static Condition<String> libraryLineHighlighted(int start, int end, String message, String colour) {
        return lineHighlighted(start, end, message, "LIBRARY", colour);
    }

    private static Condition<String> solutionLineUnderlined(int start, int end, String message, String purpose) {
        return lineUnderlined(start, end, message, "SOLUTION", purpose);
    }

    private static Condition<String> lineUnderlined(int start, int end, String message, String file, String purpose) {
        return StandardResultTestAssertions.containsString(String.format("{\"severity\":\"%s\"," +
                                            "\"type\":\"Marker\"," +
                                            "\"file\":\"%s\"," +
                                            "\"startLineNumber\":%d," +
                                            "\"endLineNumber\":%d," +
                                            "\"message\":\"%s\"}",
                purpose, file, start, end, message));
    }

    private static Condition<String> lineHighlighted(int start, int end, String message, String file, String colour) {
        return StandardResultTestAssertions.containsString(String.format("{\"className\":\"background-%s\"," +
                                            "\"type\":\"Decoration\"," +
                                            "\"file\":\"%s\"," +
                                            "\"startLineNumber\":%d," +
                                            "\"endLineNumber\":%d," +
                                            "\"message\":\"%s\"}",
                colour, file, start, end, message));
    }

}
