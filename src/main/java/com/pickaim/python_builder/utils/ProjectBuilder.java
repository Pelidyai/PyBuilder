package com.pickaim.python_builder.utils;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectBuilder {
    public static void buildProject(String projectPath, ProgressIndicator indicator, Project project) throws Exception{
        ProjectProperty property = ProjectProperty.getInstance(project);
        Map<String, ProjectComponent> componentMap = ProjectProperty.resolveComponents(projectPath, project);
        indicator.setFraction(0.0);
        double fraction = 0.0;
        double step = 1.0 / componentMap.size();
        String subText = "Loading artifact: ";
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
        File existingComponentPath = new File(ProjectProperty.getInstance(project).getPythonDir() + File.separator + component.getName());
        if(!existingComponentPath.exists()){
            return true;
        }
        //TODO solve this over-loading file
        String oldVersion = ProjectProperty.load(existingComponentPath.getPath(), ProjectProperty.VERSION_FILE).get(component.getName());
        if(oldVersion == null){
            return true;
        }
        oldVersion = ProjectProperty.resolveVersionBranch(oldVersion)[0];
        ProjectComponent oldComponent = new ProjectComponent(component.getName(), oldVersion, component.getLink(), component.getBranch(), project);
        return StringUtils.isEmpty(component.getBranch()) || oldComponent.isNeedClone(component, existingComponentPath.exists());
    }
}
