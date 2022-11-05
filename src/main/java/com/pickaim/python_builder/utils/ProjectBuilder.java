package com.pickaim.python_builder.utils;

import com.intellij.openapi.progress.ProgressIndicator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ProjectBuilder {
    private static final String SEPARATOR = "=";
    private static final String LINK_FILE = "link.properties";
    private static final String VERSION_FILE = "version.properties";
    private static String pythonDir;
    public static void buildProject(String projectPath, ProgressIndicator indicator) throws Exception{
        Map<String, String> versions = load(projectPath, VERSION_FILE);
        Map<String, String> links = load(projectPath, LINK_FILE);
        List<String> requirements = getRequirements(projectPath);
        double fraction = 0.0;
        double step = 1.0 / requirements.size();
        String[] componentPath = StringUtils.splitByWholeSeparator(projectPath, "\\");
        String subText = "Loading requirements for " + componentPath[componentPath.length - 1] + " - ";
        Map<String, String> packages = getPackages();
        for(String requirement: requirements){
            String [] pair = getRequirementNameAndVersion(requirement);
            if(!packages.containsKey(pair[0]) || isVersionLower(packages.get(pair[0]), pair[1])) {
                indicator.setText2(subText + requirement);
                Runtime.getRuntime().exec("pip install " + requirement).waitFor();
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
        indicator.setFraction(0.0);
        fraction = 0.0;
        step = 1.0 / links.size();
        subText = "Loading components for " + componentPath[componentPath.length - 1] + " - ";
        for(String key: links.keySet()) {
            indicator.setText2(subText + key);
            if (isNeedClone(key, versions.get(key))) {
                String p = pythonDir + "\\" + key;
                if(new File(p).exists()) {
                    Runtime.getRuntime().exec("cmd.exe /k rd /s /q " + p).waitFor();
                    Runtime.getRuntime().exec("cmd.exe /k mkdir " + p).waitFor();
                }
                String command = "git clone --branch release/" +
                        versions.get(key) +
                        " " + links.get(key) +
                        " " + pythonDir + "\\" +
                        key;
                Runtime.getRuntime().exec(command).waitFor();
                buildProject(p, indicator);
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
    }

    public static void setPythonDir(String pythonDir){
        ProjectBuilder.pythonDir = pythonDir;
    }

    private static Map<String, String> load(String path, String fileName) throws Exception {
        String filePath = path + '\\' + fileName;
        if(!new File(filePath).exists()){
            throw new Exception("File " + filePath + " doesn't exist.");
        }
        try (FileInputStream fileInput = new FileInputStream(filePath)) {
            String[] lines = StringUtils.splitByWholeSeparator(new String(fileInput.readAllBytes()), "\r\n");
            Map<String, String> resultMap = new HashMap<>();
            for(String line: lines){
                if(!StringUtils.isEmpty(line)){
                    String[] pair = StringUtils.split(line, SEPARATOR);
                    resultMap.put(pair[0], pair[1]);
                }
            }
            return resultMap;
        } catch (IOException e){
            throw new Exception("IOException with" + filePath + "file");
        }
    }
    
    private static boolean isNeedClone(String name, String newVersion) throws Exception{
        File path = new File(pythonDir + "\\" + name);
        if(!path.exists()){
            return true;
        }
        String oldVersion = load(path.getPath(), VERSION_FILE).get(name);
        return isVersionLower(oldVersion, newVersion);
    }

    private static boolean isVersionLower(String oldV, String newV){
        List<Integer> oldInt =  getVersionInts(oldV);
        List<Integer> newInt =  getVersionInts(newV);
        if(oldInt.size() == newInt.size()){
            for(int i = 0; i < oldInt.size(); i++){
                if(newInt.get(i) > oldInt.get(i)){
                    return true;
                }
            }
            return false;
        } else return oldInt.size() < newInt.size();
    }

    private static List<Integer> getVersionInts(String version){
        List<Integer> result = new ArrayList<>();
        List.of(StringUtils.splitByWholeSeparator(version,".")).forEach(e -> result.add(Integer.valueOf(e)));
        return result;
    }

    private static List<String> getRequirements(String path) throws Exception{
        try(FileInputStream input = new FileInputStream(path + "\\" + "requirements.txt")){
            return new ArrayList<>(Arrays.asList(StringUtils.splitByWholeSeparator(new String(input.readAllBytes()), "\r\n")));
        } catch (IOException e) {
            throw new Exception("File " + path + "\\" + "requirements.txt not found");
        }
    }

    private static Map<String, String> getPackages() throws Exception{
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
    
    private static String[] getRequirementNameAndVersion(String requirementWithVersion) throws Exception{
        if(requirementWithVersion.contains("==")){
            return StringUtils.split(requirementWithVersion, "==");
        } else if (requirementWithVersion.contains("~=")){
            return StringUtils.split(requirementWithVersion, "~=");
        } else {
            throw new Exception("Incorrect requirement " + requirementWithVersion);
        }
    }
}
