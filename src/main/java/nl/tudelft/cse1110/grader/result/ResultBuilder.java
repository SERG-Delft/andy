package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.grader.execution.ExecutionStep;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import static javax.tools.Diagnostic.Kind.ERROR;

public class ResultBuilder {

    private boolean failed;
    private StringBuilder result = new StringBuilder();
    private StringBuilder debug = new StringBuilder();

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        l("We could not compile your code. See the compilation errors below:");
        for(Diagnostic diagnostic: diagnostics) {
            if(diagnostic.getKind() == ERROR) {
                l(String.format("- line %d: %s",
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));
            }
        }
    }

    public void logFinish(ExecutionStep step) {
        d(String.format("%s finished at %s", step.getClass().getSimpleName(), now()));
    }

    public void logStart(ExecutionStep step) {
        d(String.format("%s started at %s", step.getClass().getSimpleName(), now()));
    }

    public void debug(ExecutionStep step, String msg) {
        d(String.format("%s: %s", step.getClass().getSimpleName(), msg));
    }

    public void genericFailure(ExecutionStep step, Exception e) {
        l(String.format("Oh, we are facing a failure in %s that we cannot recover from.", step.getClass().getSimpleName()));
        l("Please, send the message below to the teaching team:");
        l("---");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        l(sw.toString().trim());

        l("---");

        this.failed = true;
    }

    private void l(String line) {
        result.append(line);
        result.append("\n");
    }

    private void d(String line) {
        debug.append(line);
        debug.append("\n");
    }

    public String buildEndUserResult() {
        return result.toString();
    }

    public String buildDebugResult() {
        return debug.toString() + "\n\n" + result.toString();
    }

    private String now() {
        return LocalDateTime.now().toString();
    }
}
