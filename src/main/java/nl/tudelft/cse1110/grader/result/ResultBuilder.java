package nl.tudelft.cse1110.grader.result;

import nl.tudelft.cse1110.codechecker.engine.CheckScript;
import nl.tudelft.cse1110.grader.execution.ExecutionStep;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.engine.TestTag;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.pitest.mutationtest.tooling.CombinedStatistics;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.tools.Diagnostic.Kind.ERROR;

public class ResultBuilder {

    private boolean failed;
    private StringBuilder result = new StringBuilder();
    private StringBuilder debug = new StringBuilder();
    private StringBuilder consoleOutput = new StringBuilder();
    private OutputStream outputStream;
    private PrintStream console;
    private static HashMap<String, String> importDictionary = new HashMap<>();

    public ResultBuilder() {
        this.console = System.out;

        this.outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                consoleOutput.append((char)b);
            }
        };
    }

    public void startCapturingConsole() {
        System.setOut(new PrintStream(this.outputStream));
    }

    public void stopCapturingConsole() {
        System.setOut(this.console);
    }

    public void compilationFail(List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        dictionarySetup();
        l("We could not compile your code. See the compilation errors below:");
        for(Diagnostic diagnostic: diagnostics) {
            if(diagnostic.getKind() == ERROR) {
                l(String.format("- line %d: %s",
                        diagnostic.getLineNumber(),
                        diagnostic.getMessage(null)));

                //here go any further compilation failure tips
                //1. Missing any imports
                checkMissingImport(diagnostic.getMessage(null));
                //Further improvements: Typos (Keywords and Variables)
            }
        }
        failed();
    }

    public void logFinish(ExecutionStep step) {
        d(String.format("%s finished at %s", step.getClass().getSimpleName(), now()));
    }

    public void logStart(ExecutionStep step) {
        d(String.format("%s started at %s", step.getClass().getSimpleName(), now()));
    }
    public void logFinish() {
        d(String.format("Finished at %s", now()));
    }

    public void logStart() {
        d(String.format("Started at %s", now()));
    }

    public void debug(ExecutionStep step, String msg) {
        d(String.format("%s: %s", step.getClass().getSimpleName(), msg));
    }

    public void genericFailure(ExecutionStep step, String msg) {
        l(msg);
        d(msg);

        failed();
    }

    public void genericFailure(ExecutionStep step, Throwable e) {

        StringBuilder failureMsg = new StringBuilder();

        failureMsg.append(String.format("Oh, we are facing a failure in %s that we cannot recover from.\n", step.getClass().getSimpleName()));
        failureMsg.append("Please, send the message below to the teaching team:\n");
        failureMsg.append("---\n");
        failureMsg.append(exceptionMessage(e));
        failureMsg.append("---\n");

        genericFailure(step, failureMsg.toString());
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
            this.logJUnitFailedTest(failure);
        }

        if(summary.getTestsSucceededCount() < summary.getTestsFoundCount())
            failed();
    }

    private void logJUnitFailedTest(TestExecutionSummary.Failure failure) {
        UniqueId.Segment lastSegment = failure.getTestIdentifier().getUniqueIdObject().getLastSegment();

        switch (lastSegment.getType()) {
            case "test-template-invocation" -> {
                String methodName = this.getParameterizedMethodName(failure);
                l(String.format("\n- Parameterized test \"%s\", test case %s failed:", methodName, lastSegment.getValue()));
            }
            case "property" -> {
                l(String.format("\n- Property test \"%s\" failed (see full output below for more info):", failure.getTestIdentifier().getDisplayName()));
            }
            default -> {
                l(String.format("\n- Test \"%s\" failed:", failure.getTestIdentifier().getDisplayName()));
            }
        }

        l(String.format("%s", failure.getException()));
    }

    private String getParameterizedMethodName(TestExecutionSummary.Failure failure) {
        int endIndex = failure.getTestIdentifier().getLegacyReportingName().indexOf('(');
        return failure.getTestIdentifier().getLegacyReportingName().substring(0, endIndex);
    }

    private void failed() {
        this.failed = true;
    }

    public String buildEndUserResult() {
        return result.toString();
    }

    public String buildDebugResult() {
        return debug.toString() + "\n\n" + result.toString() + "\n\n\n\n\n" + this.consoleOutput.toString();
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

    public void logPitest(CombinedStatistics stats) {
        l("--- Mutation testing");
        l(String.format("%d/%d killed", stats.getMutationStatistics().getTotalDetectedMutations(), stats.getMutationStatistics().getTotalMutations()));
        if(stats.getMutationStatistics().getTotalDetectedMutations() < stats.getMutationStatistics().getTotalMutations())
            l("See attached report.");
    }

    public boolean isFailed() {
        return failed;
    }

    public void logCodeChecks(CheckScript script) {
        l("--- Code checks");
        l(script.generateReport());
    }

    //Will remake this as a .txt / .csv file
    private void dictionarySetup() {
        importDictionary.put("List", "import java.util.List;");
        importDictionary.put("Collection", "import java.util.Collection;");
        importDictionary.put("stream", "import java.util.stream.*;");
        importDictionary.put("LocalDate", "import java.time.LocalDate;");
        importDictionary.put("Mockito", "import static org.mockito.Mockito.*;");
        importDictionary.put("ParameterizedTest", "import org.junit.jupiter.params.ParameterizedTest;");
        importDictionary.put("Arguments", "import org.junit.jupiter.params.provider.Arguments;");
        importDictionary.put("MethodSource", "import org.junit.jupiter.params.provider.MethodSource;");
        importDictionary.put("Stream", "import java.util.stream.Stream;");
        importDictionary.put("ArrayList", "import java.util.ArrayList;");
        importDictionary.put("LinkedList", "import java.util.LinkedList;");
        importDictionary.put("assertThat", "import static org.assertj.core.api.Assertions.assertThat;");
        importDictionary.put("assertThatThrownBy", "import static org.assertj.core.api.Assertions.assertThatThrownBy;");
        importDictionary.put("BeforeEach", "import org.junit.jupiter.api.BeforeEach;");
        importDictionary.put("Test", "import org.junit.jupiter.api.Test;");
        importDictionary.put("CsvSource", "import org.junit.jupiter.params.provider.CsvSource;");
        importDictionary.put("ForAll","import net.jqwik.api.ForAll;");
        importDictionary.put("Property","import net.jqwik.api.Property;");
        importDictionary.put("IntRange","import net.jqwik.api.constraints.IntRange;");
        importDictionary.put("Size","import net.jqwik.api.constraints.Size;");
    }

    private void checkMissingImport(String message) {
        if(message.startsWith("cannot find symbol")){

            //We parse the diagnostic message and use regex to find the correct token
            //we search for the first word after first "class" instance

            Pattern p = Pattern.compile("class\\W+(\\w+)");
            Matcher m = p.matcher(message);
            String token = m.find() ? m.group(1) : null;

            if(importDictionary.containsKey(token)) {
                l(String.format("Maybe you missed the import for %s?\nTry adding this: %s\n",
                        token,
                        importDictionary.get(token)));
            }
        }
    }
}
