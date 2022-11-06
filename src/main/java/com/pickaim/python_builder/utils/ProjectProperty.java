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
    public static final String EQ_SEPARATOR = "=";
    public static final String VB_SEPARATOR = ":";
    public static final String LINK_FILE = "link.properties";
    public static final String VERSION_FILE = "version.properties";
    private static String projectPath = "";
    private static String pythonDir = "";

    private static Set<ProjectComponent> projectComponents;

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

    public static void initComponents(){
        try {
            getComponents(projectPath);
        } catch (Exception e) {
            Notifications.Bus.notify(new Notification("util-settings", "Components error", e.getMessage(), NotificationType.ERROR));
        }
    }

    public static Set<ProjectComponent> getProjectComponents() {
        return projectComponents;
    }

    public static Map<String, ProjectComponent> getComponents(String path) throws Exception{
        Map<String, String> versions = load(path, VERSION_FILE);
        Map<String, String> links = load(path, LINK_FILE);
        Map<String, ProjectComponent> result = new HashMap<>();
        for(String name: links.keySet()){
            if(!StringUtils.isEmpty(versions.get(name))) {
                String[] vB = getVersionBranch(versions.get(name));
                result.put(name, new ProjectComponent(name, vB[0], links.get(name), vB[1]));
            } else {
                result.put(name, new ProjectComponent(name, "", links.get(name), ""));
            }
        }
        if(path.equals(projectPath)){
            projectComponents = new HashSet<>(result.values());
        }
        return result;
    }

    public static Map<String, String> load(String path, String fileName) throws Exception {
        String filePath = path + File.separator + fileName;
        Map<String, String> resultMap = new HashMap<>();
        try (FileInputStream fileInput = new FileInputStream(filePath)) {
            String[] lines = StringUtils.splitByWholeSeparator(new String(fileInput.readAllBytes()), "\r\n");
            for(String line: lines){
                if(!StringUtils.isEmpty(line)){
                    String[] pair = StringUtils.split(line, EQ_SEPARATOR);
                    if(pair.length == 2) {
                        resultMap.put(pair[0], pair[1]);
                    } else {
                        resultMap.put(pair[0], "");   
                    }
                }
            }
            return resultMap;
        } catch (IOException e){
            if(projectPath.equals(path)) {
                throw new Exception("IOException with" + filePath + "file");
            } else {
                return resultMap;
            }
        }
    }

    public static String[] getVersionBranch(String input){
        String[] result = new String[2];
        if(input.contains(VB_SEPARATOR)) {
            int idx = input.indexOf(VB_SEPARATOR);
            result[0] = input.substring(0, idx);
            result[1] = input.substring(idx + 1);
        } else {
            result[0] = "";
            result[1] = input;
        }
        return result;
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

    public static List<String> getRequirements(String path){
        try(FileInputStream input = new FileInputStream(path + File.separator + "requirements.txt")){
            return new ArrayList<>(Arrays.asList(StringUtils.splitByWholeSeparator(new String(input.readAllBytes()), "\r\n")));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static Map<String, String> getPackages() throws Exception{
        Process packagesGetting = Runtime.getRuntime().exec("pip freeze");
        packagesGetting.waitFor();
        String[] packages = StringUtils.splitByWholeSeparator(new String(packagesGetting.getInputStream().readAllBytes()), "\r\n");
        Map<String, String> result = new HashMap<>();
        for(String pack: packages){
            if(!StringUtils.isEmpty(pack)) {
                String[] pair = StringUtils.split(pack, "==");
                result.put(pair[0], pair[1]);
            }
        }
        return result;
    }

    public static String getProjectPath() {
        return projectPath;
    }

    public static void setProjectPath(String projectPath) {
        ProjectProperty.projectPath = projectPath;
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
