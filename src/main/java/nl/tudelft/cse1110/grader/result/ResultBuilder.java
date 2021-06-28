package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

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

        StringBuilder failureMsg = new StringBuilder();

        failureMsg.append(String.format("Oh, we are facing a failure in %s that we cannot recover from.\n", step.getClass().getSimpleName()));
        failureMsg.append("Please, send the message below to the teaching team:\n");
        failureMsg.append("---\n");

        String messageToAppend = exceptionMessage(e);
        failureMsg.append(messageToAppend);

        failureMsg.append("---\n");

        l(failureMsg.toString());
        d(failureMsg.toString());

        this.failed = true;
    }

    @NotNull
    private String exceptionMessage(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String messageToAppend = sw.toString().trim();
        return messageToAppend;
    }

    public void logJUnitRun(TestExecutionSummary summary) {
        l("--- JUnit execution");
        l(String.format("%d/%d passed", summary.getTestsSucceededCount(), summary.getTestsFoundCount()));

        for (TestExecutionSummary.Failure failure : summary.getFailures()) {
            l(String.format("\n- Test %s failed:", failure.getTestIdentifier().getDisplayName()));
            l(exceptionMessage(failure.getException()));
        }
    }

    public String buildEndUserResult() {
        return result.toString();
    }

    public String buildDebugResult() {
        return debug.toString() + "\n\n" + result.toString();
    }


    private void l(String line) {
        result.append(line);
        result.append("\n");
    }

    private void d(String line) {
        debug.append(line);
        debug.append("\n");
    }


    private String now() {
        return LocalDateTime.now().toString();
    }

}
