package nl.tudelft.cse1110.andy.execution.externalprocess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CommandExternalProcess implements ExternalProcess {

    private final String command;
    private final String initSignal;
    private final CountDownLatch initSignalLatch;

    private Process process;

    public CommandExternalProcess(String command, String initSignal) {
        this.command = command;
        this.initSignal = initSignal;
        this.initSignalLatch = new CountDownLatch(1);
    }

    @Override
    public void launch() throws IOException {
        Thread thread = new Thread(new OutputHandler());
        process = Runtime.getRuntime().exec(command);

        process.onExit().thenAccept(p -> initSignalLatch.countDown());

        thread.start();
    }

    @Override
    public void awaitInitialization() {
        // In case await was called but no process was launched.
        if (process == null) {
            return;
        }

        // Block thread until process has initialized
        try {
            this.initSignalLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void kill() {
        boolean processIsRunning = process != null && process.isAlive();
        if (!processIsRunning) {
            return;
        }

        process.destroy();
    }

    @Override
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
                    initSignalLatch.countDown();
                }
            }
        }
    }
}
