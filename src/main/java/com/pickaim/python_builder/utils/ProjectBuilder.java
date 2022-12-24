package com.pickaim.python_builder.utils;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectBuilder {
    public static void buildProject(String projectPath, ProgressIndicator indicator, Project project) throws Exception{
        ProjectProperty property = ProjectProperty.getInstance(project);
        Map<String, ProjectComponent> componentMap = ProjectProperty.resolveComponents(projectPath, project);
        Map<String, Pair<String, String>> requirements = ProjectProperty.resolveRequirements(projectPath);
        double fraction = 0.0;
        double step = 1.0 / requirements.size();
        String subText = "Loading requirement: ";
        Map<String, Pair<String, String>> packages = ProjectProperty.resolvePackages();
        for(String key: requirements.keySet()){
            Pair<String, String> pair = requirements.get(key);
            if(!packages.containsKey(pair.getKey())
                    || !packages.get(key).getValue().equals(pair.getValue())) {
                indicator.setText2(subText + key);
                ProcessRunner.runCommand("pip install " + key + "==" + requirements.get(key).getValue());
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
        indicator.setFraction(0.0);
        fraction = 0.0;
        step = 1.0 / (componentMap.size() - 1);
        subText = "Loading artifact: ";
        for(String name: componentMap.keySet()) {
            indicator.setText2(subText + name);
            ProjectComponent component = componentMap.get(name);
            if (isNeedClone(component, project)) {
                String p = property.getPythonDir() + File.separator + name;
                if(new File(p).exists()) {
                    FileUtils.deleteDirectory(new File(p));
                    Files.createDirectory(Paths.get(p));
                }
                component.cloning();
                buildProject(p, indicator, project);
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
    }

    private static boolean isNeedClone(ProjectComponent component, Project project) {
        if(component.getName().equals(ProjectProperty.getInstance(project).getProjectName())){
            return false;
        }
        File path = new File(ProjectProperty.getInstance(project).getPythonDir() + File.separator + component.getName());
        if(!path.exists()){
            return true;
        }
        //TODO solve this over-loading file
        String oldVersion = ProjectProperty.load(path.getPath(), ProjectProperty.VERSION_FILE).get(component.getName());
        if(oldVersion == null){
            return true;
        }
        oldVersion = ProjectProperty.resolveVersionBranch(oldVersion)[0];
        ProjectComponent oldComponent = new ProjectComponent(component.getName(), oldVersion, component.getLink(), component.getBranch(), project);
        return StringUtils.isEmpty(component.getBranch()) || oldComponent.isNeedClone(component);
    }
}
