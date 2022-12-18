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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ProjectProperty {
    //#region Constants
    public static final String EQ_SEPARATOR = "=";
    public static final String VB_SEPARATOR = ":";
    public static final String LINK_FILE = "link.properties";
    public static final String VERSION_FILE = "version.properties";
    private static final String NEXUS_NAME = "nexus_link";
    //#endregion

    //#region Private fields
    private static String nexusLink = "";
    private static String projectPath = "";
    private static String projectName = "";
    private static String pythonDir = "";
    private static Map<String, ProjectComponent> projectComponents;
    
    //#endregion

    //#region Public methods
    public static void checkInterpreter(){
        if(StringUtils.isEmpty(pythonDir)) {
           resetInterpreter();
        }
    }

    public static void resetInterpreter(){
        pythonDir = chooseInterpreter();
        Notifications.Bus.notify(new Notification("util-settings", "Interpreter setting",
                "Interpreter was set to " + pythonDir, NotificationType.INFORMATION));
    }

    public static void resolveComponents(){
        try {
            resolveComponents(projectPath);
            checkInterpreter();
        } catch (Exception e) {
            Notifications.Bus.notify(new Notification("util-settings", "Components error", e.getMessage(), NotificationType.ERROR));
        }
    }

    public static Map<String, ProjectComponent> resolveComponents(String path) throws Exception{
        Map<String, String> versions = load(path, VERSION_FILE);
        Map<String, String> links = load(path, LINK_FILE);
        Map<String, ProjectComponent> result = new HashMap<>();
        if(StringUtils.isEmpty(nexusLink)) {
            nexusLink = links.get(NEXUS_NAME);
        }
        for(String name: versions.keySet()){
            if(!StringUtils.isEmpty(versions.get(name))) {
                String[] vB = resolveVersionBranch(versions.get(name));
                result.put(name, new ProjectComponent(name, vB[0], links.get(name), vB[1]));
            } else {
                result.put(name, new ProjectComponent(name, "", links.get(name), ""));
            }
        }
        if(path.equals(projectPath)){
            projectComponents = result;
        }
        return result;
    }
    
    public static String[] resolveVersionBranch(String input){
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

    private static String chooseInterpreter(){
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

    public static Map<String, Pair<String, String>> resolveRequirements(String path) throws Exception{
        try(FileInputStream input = new FileInputStream(path + File.separator + "requirements.txt")){
            String[] requirements = StringUtils.splitByWholeSeparator(new String(input.readAllBytes()), "\r\n"); 
            return extractNameVersionPairs(requirements);
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    public static Map<String, Pair<String, String>> resolvePackages() throws Exception{
        String processResult = ProcessRunner.runCommand("pip freeze");
        String[] packages = StringUtils.splitByWholeSeparator(processResult, "\r\n");
        return extractNameVersionPairs(packages);
    }

    //#endregion
    
    //#region Private methods
    private static Map<String, Pair<String, String>> extractNameVersionPairs(String[] strings) throws Exception{
        Map<String, Pair<String, String>> result = new HashMap<>();
        for(String pack: strings){
            if(!StringUtils.isEmpty(pack)) {
                String[] pair = resolveRequirementNameVersion(pack);
                result.put(pair[0], new ImmutablePair<>(pair[0], pair[1]));
            }
        }
        return result;
    }

    private static String[] resolveRequirementNameVersion(String requirementWithVersion) throws Exception{
        if(requirementWithVersion.contains("==")){
            return StringUtils.split(requirementWithVersion, "==");
        } else if (requirementWithVersion.contains("~=")){
            return StringUtils.split(requirementWithVersion, "~=");
        } else {
            throw new Exception("Incorrect requirement " + requirementWithVersion);
        }
    }

    //#endregion

    //#region Setters
    public static void setProjectPath(String projectPath) {
        ProjectProperty.projectPath = projectPath;
    }

    public static void setProjectName(String projectName){
        ProjectProperty.projectName= projectName;
    }

    //#endregion

    //#region Getters
    public static String getPythonDir(){
        return pythonDir;
    }

    public static Map<String, ProjectComponent> getProjectComponents() {
        return projectComponents;
    }

    public static String getProjectPath() {
        return projectPath;
    }

    public static String getNexusLink(){
        return nexusLink;
    }

    public static String getProjectName() {
        return projectName;
    }

    public static ProjectComponent getCurrentComponent(){
        return projectComponents.get(projectName);
    }

    //#endregion

}
