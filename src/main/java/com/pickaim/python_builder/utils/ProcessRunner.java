package com.pickaim.python_builder.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProcessRunner {
    public static String runCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        String errors = new String(process.getErrorStream().readAllBytes());
        String output = new String(process.getInputStream().readAllBytes());
        process.destroy();
        if (process.exitValue() != 0 && !errors.isEmpty()
                && !errors.contains("No stash entries found") && !errors.contains("Switched to branch")) {
            throw new Exception(errors);
        }
        return output;
    }

    public static String runCommand(String command, long timeoutInSeconds) throws Exception {
        Process process;
        try {
            process = executeCommandLine(command, timeoutInSeconds * 1000).getProcess();
        } catch (TimeoutException e) {
            return "";
        }
        String errors = new String(process.getErrorStream().readAllBytes());
        String output = new String(process.getInputStream().readAllBytes());
        process.destroy();
        if (process.exitValue() != 0 && !errors.isEmpty() && !errors.contains("No stash entries found")) {
            throw new Exception(errors);
        }
        return output;
    }

    private static Worker executeCommandLine(final String commandLine, final long timeout)
            throws IOException, InterruptedException, TimeoutException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(commandLine);

        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null)
                return worker;
            else
                throw new TimeoutException();
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroyForcibly();
        }
    }

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;

        private Worker(Process process) {
            this.process = process;
        }

        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
            }
        }

        public Process getProcess() {
            return process;
        }
    }
}
