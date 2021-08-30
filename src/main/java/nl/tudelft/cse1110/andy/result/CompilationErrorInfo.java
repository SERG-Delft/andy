package nl.tudelft.cse1110.andy.result;

public class CompilationErrorInfo {
    private final String name;
    private final long lineNumber;
    private final String message;

    public CompilationErrorInfo(String name, long lineNumber, String message) {
        this.name = name;
        this.lineNumber = lineNumber;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }
}
