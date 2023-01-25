package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.annotations.SerializedName;

public record EditorFeedbackRange(EditorFeedbackLocation location,
                                  long startLineNumber, long endLineNumber,
                                  EditorFeedbackSeverity severity,
                                  String message) {

    /*
     * [
     *     {
     *         "location": "SOLUTION", //one of ["LIBRARY", "SOLUTION", "TEST"]
     *         "startLineNumber": 20, //int
     *         "endLineNumber": 20, //int
     *         "severity": "Info", //one of ["Error", "Hint", "Info", "Warning"]
     *         "message": "100% coverage" //String
     *     },
     *     {
     *         "location": "LIBRARY",
     *         "startLineNumber": 41,
     *         "endLineNumber": 48,
     *         "severity": "Info",
     *         "message": "Some message"
     *     },
     *   [...]
     * ]
     */

    public enum EditorFeedbackSeverity {
        @SerializedName("Error")
        ERROR,
        @SerializedName("Hint")
        HINT,
        @SerializedName("Info")
        INFO,
        @SerializedName("Warning")
        WARNING
    }

    public enum EditorFeedbackLocation {
        @SerializedName("LIBRARY")
        LIBRARY,
        @SerializedName("SOLUTION")
        SOLUTION
    }
}
