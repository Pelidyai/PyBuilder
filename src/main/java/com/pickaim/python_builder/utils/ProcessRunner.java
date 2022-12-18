package com.pickaim.python_builder.utils;
public class ProcessRunner {
    public static String runCommand(String command) throws Exception{
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        String errors = new String(process.getErrorStream().readAllBytes());
        String output = new String(process.getInputStream().readAllBytes());
        if(process.exitValue() != 0 && !errors.isEmpty()){
            throw new Exception(errors);
        }
        return output;
    }
}
