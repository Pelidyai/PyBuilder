package com.pickaim.python_builder.utils;

import com.intellij.openapi.progress.ProgressIndicator;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectBuilder {
    public static void buildProject(String projectPath, ProgressIndicator indicator) throws Exception{
        Map<String, ProjectComponent> componentMap = ProjectProperty.resolveComponents(projectPath);
        Map<String, Pair<String, String>> requirements = ProjectProperty.resolveRequirements(projectPath);
        double fraction = 0.0;
        double step = 1.0 / requirements.size();
        String subText = "Loading requirement: ";
        Map<String, Pair<String, String>> packages = ProjectProperty.resolvePackages();
        for(String key: requirements.keySet()){
            Pair<String, String> pair = requirements.get(key);
            if(!packages.containsKey(pair.getKey()) || VersionUtils.isVersionLower(packages.get(key).getValue(), pair.getValue())) {
                indicator.setText2(subText + key);
                Runtime.getRuntime().exec("pip install " + key).waitFor();
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
        indicator.setFraction(0.0);
        fraction = 0.0;
        step = 1.0 / componentMap.size();
        subText = "Loading artifact: ";
        for(String name: componentMap.keySet()) {
            indicator.setText2(subText + name);
            ProjectComponent component = componentMap.get(name);
            if (isNeedClone(component)) {
                String p = ProjectProperty.getPythonDir() + File.separator + name;
                if(new File(p).exists()) {
                    FileUtils.deleteDirectory(new File(p));
                    Files.createDirectory(Paths.get(p));
                }
                component.cloning();
                buildProject(p, indicator);
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
    }

    private static boolean isNeedClone(ProjectComponent component) throws Exception{
        if(component.getName().equals(ProjectProperty.getProjectName())){
            return false;
        }
        File path = new File(ProjectProperty.getPythonDir() + File.separator + component.getName());
        if(!path.exists()){
            return true;
        }
        //TODO solve this over-loading file
        String oldVersion = ProjectProperty.load(path.getPath(), ProjectProperty.VERSION_FILE).get(component.getName());
        if(oldVersion == null){
            return true;
        }
        oldVersion = ProjectProperty.resolveVersionBranch(oldVersion)[0];
        ProjectComponent oldComponent = new ProjectComponent(component.getName(), oldVersion, component.getLink(), component.getBranch());
        return StringUtils.isEmpty(component.getBranch()) || oldComponent.isLower(component);
    }
}
