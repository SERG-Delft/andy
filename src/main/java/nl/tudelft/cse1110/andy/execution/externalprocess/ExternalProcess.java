package nl.tudelft.cse1110.andy.execution.externalprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ExternalProcess {

    private final String command;
    private final String initSignal;
    private boolean initSignalFound;

    private Process process;

    public ExternalProcess(String command, String initSignal) {
        this.command = command;
        this.initSignal = initSignal;
        this.initSignalFound = false;
    }

    public void launch() throws IOException {
        Thread thread = new Thread(new OutputHandler());
        process = Runtime.getRuntime().exec(command);

        thread.start();
    }

    public void await() {
        // In case await was called but no process was launched.
        if (process == null) {
            return;
        }

        while (process.isAlive() && !initSignalFound) ;
    }

    public void kill() {
        boolean processIsRunning = process != null && process.isAlive();
        if (!processIsRunning) {
            return;
        }

        process.destroy();
    }

    public String getErr() {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while (reader.ready()) {
                String next = reader.readLine();
                sb.append(next);
            }

            if (sb.isEmpty()) {
                return null;
            }

            return sb.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    private class OutputHandler implements Runnable {
        @Override
        public void run() {
            Scanner data = new Scanner(process.getInputStream());

            while (data.hasNextLine()) {
                String line = data.nextLine();
                if (line.contains(initSignal)) {
                    initSignalFound = true;
                }
            }
        }
    }
}
