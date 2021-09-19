package nl.tudelft.cse1110.andy.execution.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ExternalProcess extends Thread {

    private final String command;
    private final String endSignal;
    private final StringBuilder output;

    private Process process;

    public ExternalProcess(String command, String endSignal) {
        this.command = command;
        this.endSignal = endSignal;
        this.output = new StringBuilder();
    }

    public String getCommand() {
        return command;
    }

    public String getEndSignal() {
        return endSignal;
    }

    public void launch() throws IOException {
        Thread thread = new Thread(this);
        process = Runtime.getRuntime().exec(command);

        thread.start();
    }

    @Override
    public void run() {
        Scanner data = new Scanner(process.getInputStream());

        while (data.hasNextLine()) {
            String line = data.nextLine();
            output.append(line);
        }
    }

    public void await() {
        // In case await was called but no process was launched.
        if (process == null) {
            return;
        }

        while (process.isAlive() && !output.toString().contains(endSignal)) { }
    }

    public void kill() {
        if (process == null) {
            return;
        }


        process.destroy();
    }
}
