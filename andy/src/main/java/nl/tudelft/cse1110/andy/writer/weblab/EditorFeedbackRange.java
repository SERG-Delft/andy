package nl.tudelft.cse1110.andy.writer.weblab;

import com.google.gson.annotations.SerializedName;

public abstract class EditorFeedbackRange {
    /*
     * [
     *     {
     *         "type": "Marker", // one of ["Marker", "Decoration"]
     *         "file": "SOLUTION", //one of ["LIBRARY", "SOLUTION", "TEST"]
     *         "startLineNumber": 20, //int
     *         "endLineNumber": 20, //int
     *         "severity": "Info", //one of ["Error", "Hint", "Info", "Warning"], only if type is "Marker"
     *         "message": "100% coverage" //String
     *          // "startColumn": 1, //optional, int
     *          // "endColumn": 20 //optional, int
     *     },
     *     {
     *         "type": "Decoration", // one of ["Marker", "Decoration"]
     *         "file": "LIBRARY",
     *         "startLineNumber": 41,
     *         "endLineNumber": 48,
     *         "className": "background-blue", // one of ["background-blue", "background-red", "background-green", "background-yellow"], only if type is "Decoration"
     *         "message": "Some message"
     *     },
     *   [...]
     * ]
     */

    private final EditorFeedbackType type;
    private final EditorFeedbackFile file;
    private final long startLineNumber;
    private final long endLineNumber;
    private final String message;

    protected EditorFeedbackRange(EditorFeedbackType type,
                                  EditorFeedbackFile file,
                                  long startLineNumber, long endLineNumber,
                                  String message) {
        this.type = type;
        this.file = file;
        this.startLineNumber = startLineNumber;
        this.endLineNumber = endLineNumber;
        this.message = message;
    }

    public EditorFeedbackType type() {
        return type;
    }

    public EditorFeedbackFile file() {
        return file;
    }

    public long startLineNumber() {
        return startLineNumber;
    }

    public long endLineNumber() {
        return endLineNumber;
    }

    public String message() {
        return message;
    }

    public enum EditorFeedbackFile {
        @SerializedName("LIBRARY")
        LIBRARY,
        @SerializedName("SOLUTION")
        SOLUTION
    }

    public enum EditorFeedbackType {
        @SerializedName("Marker")
        MARKER,
        @SerializedName("Decoration")
        DECORATION
    }
}

class EditorFeedbackRangeUnderline extends EditorFeedbackRange {
    private final EditorFeedbackSeverity severity;

    protected EditorFeedbackRangeUnderline(EditorFeedbackFile file,
                                           long startLineNumber,
                                           long endLineNumber,
                                           String message,
                                           EditorFeedbackSeverity severity) {
        super(EditorFeedbackType.MARKER, file, startLineNumber, endLineNumber, message);
        this.severity = severity;
    }

    public EditorFeedbackSeverity severity() {
        return severity;
    }

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
}
