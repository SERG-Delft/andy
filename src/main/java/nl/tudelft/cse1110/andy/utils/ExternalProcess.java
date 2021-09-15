package nl.tudelft.cse1110.andy.utils;

import java.io.IOException;
import java.io.InputStream;

public class ExternalProcess {

    private String command;
    private String endSignal;

    private Process process;

    public ExternalProcess(String command, String endSignal) {
        this.command = command;
        this.endSignal = endSignal;
    }

    public String getCommand() {
        return command;
    }

    public String getEndSignal() {
        return endSignal;
    }

    public void launch() throws IOException {
        process = Runtime.getRuntime().exec(command);
    }

    public void await() throws IOException {
        // In case await was called but no process was launched.
        if (process == null) {
            return;
        }

        while (true) {
            byte[] bytes = process.getInputStream().readAllBytes();

            if (new String(bytes).contains(endSignal)) {
                break;
            }
        }
    }

    public void kill() {
        if (process == null) {
            return;
        }

        process.destroyForcibly();
    }
}
