package com.pickaim.python_builder.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.jetbrains.python.sdk.PythonSdkUtil;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ProjectProperty {
    
    private static String pythonDir = "";

    private static final String EQ_SEPARATOR = "=";
    private static final String VB_SEPARATOR = ":";
    public static final String LINK_FILE = "link.properties";
    public static final String VERSION_FILE = "version.properties";

    public static String getPythonDir(){
        return pythonDir;
    }

    public static void checkInterpreter(){
        if(StringUtils.isEmpty(pythonDir)) {
           resetInterpreter();
        }
    }

    public static void resetInterpreter(){
        pythonDir = getSelectedInterpreter();
        Notifications.Bus.notify(new Notification("util-settings", "Interpreter setting",
                "Interpreter was set to " + pythonDir, NotificationType.INFORMATION));
    }

    public static Map<String, ProjectComponent> getComponents(String path) throws Exception{
        Map<String, String> versions = load(path, VERSION_FILE);
        Map<String, String> links = load(path, LINK_FILE);
        Map<String, ProjectComponent> result = new HashMap<>();
        for(String name: links.keySet()){
            if(!StringUtils.isEmpty(versions.get(name))) {
                String[] versionBranch = StringUtils.split(versions.get(name), VB_SEPARATOR);
                String branch = "";
                if (versions.get(name).contains(VB_SEPARATOR)) {
                    branch = versionBranch[1];
                }
                result.put(name, new ProjectComponent(name, versionBranch[0], links.get(name), branch));
            } else {
                result.put(name, new ProjectComponent(name, "", links.get(name), ""));
            }
        }
        return result;
    }

    public static Map<String, String> load(String path, String fileName) throws Exception {
        String filePath = path + '\\' + fileName;
        if(!new File(filePath).exists()){
            throw new Exception("File " + filePath + " doesn't exist.");
        }
        try (FileInputStream fileInput = new FileInputStream(filePath)) {
            String[] lines = org.apache.commons.lang3.StringUtils.splitByWholeSeparator(new String(fileInput.readAllBytes()), "\r\n");
            Map<String, String> resultMap = new HashMap<>();
            for(String line: lines){
                if(!org.apache.commons.lang3.StringUtils.isEmpty(line)){
                    String[] pair = org.apache.commons.lang3.StringUtils.split(line, EQ_SEPARATOR);
                    resultMap.put(pair[0], pair[1]);
                }
            }
            return resultMap;
        } catch (IOException e){
            throw new Exception("IOException with" + filePath + "file");
        }
    }

    private static String getSelectedInterpreter(){
        List<Sdk> pythonSdks = PythonSdkUtil.getAllSdks();
        if (pythonSdks.isEmpty()) {
            Messages.showErrorDialog("Python interpreter not found", "Python Interpreter Problem");
            return "";
        }
        int sdkIdx = 0;
        if (pythonSdks.size() > 1) {
            String[] variants = new String[pythonSdks.size()];
            for (int i = 0; i < pythonSdks.size(); i++) {
                variants[i] = pythonSdks.get(i).getHomePath();
            }
            String selected = Messages.showEditableChooseDialog("Select interpreter", "Interpreter Selecting",
                    Messages.getQuestionIcon(), variants, variants[0], new NonEmptyInputValidator());
            sdkIdx = List.of(variants).indexOf(selected);
        }
        return Objects.requireNonNull(PythonSdkUtil.getSitePackagesDirectory(pythonSdks.get(sdkIdx))).getPath();
    }

    public static List<String> getRequirements(String path) throws Exception{
        try(FileInputStream input = new FileInputStream(path + "\\" + "requirements.txt")){
            return new ArrayList<>(Arrays.asList(org.apache.commons.lang3.StringUtils.splitByWholeSeparator(new String(input.readAllBytes()), "\r\n")));
        } catch (IOException e) {
            throw new Exception("File " + path + "\\" + "requirements.txt not found");
        }
    }

    public static Map<String, String> getPackages() throws Exception{
        Process packagesGetting = Runtime.getRuntime().exec("pip freeze");
        packagesGetting.waitFor();
        String[] packages = org.apache.commons.lang3.StringUtils.splitByWholeSeparator(new String(packagesGetting.getInputStream().readAllBytes()), "\r\n");
        Map<String, String> result = new HashMap<>();
        for(String pack: packages){
            if(!org.apache.commons.lang3.StringUtils.isEmpty(pack)) {
                String[] pair = org.apache.commons.lang3.StringUtils.split(pack, "==");
                result.put(pair[0], pair[1]);
            }
        }
        return result;
    }

    public static String[] getRequirementNameAndVersion(String requirementWithVersion) throws Exception{
        if(requirementWithVersion.contains("==")){
            return org.apache.commons.lang3.StringUtils.split(requirementWithVersion, "==");
        } else if (requirementWithVersion.contains("~=")){
            return org.apache.commons.lang3.StringUtils.split(requirementWithVersion, "~=");
        } else {
            throw new Exception("Incorrect requirement " + requirementWithVersion);
        }
    }

}
