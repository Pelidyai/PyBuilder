package com.pickaim.python_builder.utils;

import com.intellij.ide.SaveAndSyncHandler;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.jetbrains.python.sdk.PythonSdkUtil;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.io.*;
import java.util.*;

public class ProjectProperty {
    //#region Constants
    public static final String EQ_SEPARATOR = "=";
    public static final String VB_SEPARATOR = ":";
    public static final String LINK_FILE = "link.properties";
    public static final String VERSION_FILE = "version.properties";
    private static final String NEXUS_NAME = "nexus_link";
    private static final String CONFIG_FILE = "config.pickaim";
    //#endregion

    //#region Private fields
    private String nexusLink = "";
    private String pythonDir;
    private Map<String, ProjectComponent> projectComponents;
    private final Project project;
    private final String projectName;
    private final String configPath;
    private final String projectPath;
    private static final Map<Project, ProjectProperty> propertiesMap = new HashMap<>();
    //#endregion

    //#region Public methods
    public static ProjectProperty getInstance(Project project){
        if(!propertiesMap.containsKey(project)){
            propertiesMap.put(project, new ProjectProperty(project));
        }
        return propertiesMap.get(project);
    }

    private ProjectProperty(Project project){
        this.project = project;
        projectPath = project.getBasePath();
        projectName = project.getName();
        configPath = projectPath +
                File.separator + ".idea" +
                File.separator + CONFIG_FILE;
    }

    public void checkInterpreter() {
        try {
            File configFile = new File(configPath);
            if(!configFile.exists()){
                boolean isSuccessful = configFile.createNewFile();
                if(!isSuccessful){
                    throw new RuntimeException("Cannot open: " + configPath);
                }
            }
            if(configFile.length() != 0 && StringUtils.isEmpty(pythonDir)){
                ObjectInputStream saveInputStream = new ObjectInputStream(
                        new FileInputStream(configFile)
                );
                pythonDir = (String) saveInputStream.readObject();
            }
            if (StringUtils.isEmpty(pythonDir)) {
                resetInterpreter();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetInterpreter(){
        pythonDir = chooseInterpreter();
        Notifications.Bus.notify(new Notification("util-settings", "Interpreter setting",
                "Interpreter was set to " + pythonDir, NotificationType.INFORMATION));
        File configFile = new File(configPath);
        try {
            ObjectOutputStream saveStream = new ObjectOutputStream(
                    new FileOutputStream(configFile)
            );
            saveStream.writeObject(pythonDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(){
        try {
            updateMe();
            checkInterpreter();
        } catch (Exception e) {
            Notifications.Bus.notify(new Notification("util-settings", "Update error", e.getMessage(), NotificationType.ERROR));
        }
    }

    public static Map<String, ProjectComponent> resolveComponents(String path, Project project){
        Map<String, String> versions = load(path, VERSION_FILE);
        Map<String, String> links = load(path, LINK_FILE);
        Map<String, ProjectComponent> result = new HashMap<>();
        for(String name: versions.keySet()){
            if(!StringUtils.isEmpty(versions.get(name))) {
                String[] vB = resolveVersionBranch(versions.get(name));
                result.put(name, new ProjectComponent(name, vB[0], links.get(name), vB[1], project));
            } else {
                result.put(name, new ProjectComponent(name, "", links.get(name), "", project));
            }
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
    
    public static Map<String, String> load(String path, String fileName) {
        String filePath = path + File.separator + fileName;
        Map<String, String> resultMap = new HashMap<>();
        try (FileInputStream fileInput = new FileInputStream(filePath)) {
            String[] lines = StringUtils.splitByWholeSeparator(new String(fileInput.readAllBytes()), "\r\n");
            for (String line : lines) {
                if (!StringUtils.isEmpty(line)) {
                    String[] pair = StringUtils.split(line, EQ_SEPARATOR);
                    if (pair.length == 2) {
                        resultMap.put(pair[0], pair[1]);
                    } else {
                        resultMap.put(pair[0], "");
                    }
                }
            }
            return resultMap;
        } catch (IOException e) {
            return resultMap;
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

    public static void saveChanges(){
        FileDocumentManager documentManager = FileDocumentManager.getInstance();
        for(Document unsavedDoc: documentManager.getUnsavedDocuments()){
            documentManager.saveDocument(unsavedDoc);
        }
        SaveAndSyncHandler.getInstance().refreshOpenFiles();
        VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
    }
    //#endregion
    
    //#region Private methods
    private void updateMe(){
        Map<String, String> links = load(projectPath, LINK_FILE);
        nexusLink = links.get(NEXUS_NAME);
        projectComponents = resolveComponents(projectPath, project);
    }

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

    //#endregion

    //#region Getters
    public String getPythonDir(){
        return pythonDir;
    }

    public Map<String, ProjectComponent> getProjectComponents() {
        return projectComponents;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getNexusLink(){
        return nexusLink;
    }

    public String getProjectName() {
        return projectName;
    }

    public ProjectComponent getCurrentComponent(){
        return projectComponents.get(projectName);
    }

    //#endregion

}
