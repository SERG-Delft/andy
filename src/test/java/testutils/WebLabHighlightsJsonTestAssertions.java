package testutils;

import org.assertj.core.api.Condition;

import static testutils.WebLabTestAssertions.containsString;

public class WebLabHighlightsJsonTestAssertions {

    public static Condition<String> highlightLineFullyCovered(int line) {
        return line(line, "100% coverage", "FULL_COVERAGE");
    }

    public static Condition<String> highlightLinePartiallyCovered(int line) {
        return line(line, "Partial coverage", "PARTIAL_COVERAGE");
    }

    public static Condition<String> highlightLineNotCovered(int line) {
        return line(line, "No coverage", "NO_COVERAGE");
    }

    private static Condition<String> line(int line, String message, String purpose) {
        return containsString("{\"line\":" + line + "," +
                              "\"message\":\"" + message + "\"," +
                              "\"location\":\"LIBRARY\"," +
                              "\"purpose\":\"" + purpose + "\"}");
    }

}
