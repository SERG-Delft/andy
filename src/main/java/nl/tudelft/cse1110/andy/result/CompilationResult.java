package nl.tudelft.cse1110.andy.result;

import java.util.Collections;
import java.util.List;

public class CompilationResult {

    private final List<CompilationErrorInfo> errors;
    private final boolean wasExecuted;

    private CompilationResult(boolean wasExecuted, List<CompilationErrorInfo> errors) {
        this.wasExecuted = wasExecuted;
        this.errors = errors;
    }

    public static CompilationResult compilationOk() {
        return new CompilationResult(true, Collections.emptyList());
    }

    public static CompilationResult compilationFail(List<CompilationErrorInfo> errors) {
        return new CompilationResult(true, errors);
    }

    public static CompilationResult empty() {
        return new CompilationResult(false, Collections.emptyList());
    }

    public List<CompilationErrorInfo> getErrors() {
        return errors;
    }

    public boolean successful() {
        return errors == null || errors.isEmpty();
    }

    public boolean wasExecuted() {
        return wasExecuted;
    }
}
