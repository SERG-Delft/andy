package nl.tudelft.cse1110.andy.execution.process;

import java.io.IOException;
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
            output.append(line).append('\n');
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
        boolean processIsRunning = process != null && process.isAlive();
        if (!processIsRunning) {
            return;
        }

        process.destroy();
    }

    public String getOutput(){
        return output.toString();
    }

    public String getErr(){
        StringBuilder sb = new StringBuilder();
        Scanner error = new Scanner(process.getErrorStream());
        while(error.hasNextLine()){
            sb.append(error.nextLine());
        }

        if(sb.isEmpty()){
            return null;
        }

        return sb.toString();
    }
}
