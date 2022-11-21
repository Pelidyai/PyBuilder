package com.pickaim.python_builder.utils;

import com.intellij.openapi.progress.ProgressIndicator;
import com.pickaim.python_builder.ProjectComponent;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProjectBuilder {
    public static void buildProject(String projectPath, ProgressIndicator indicator) throws Exception{
        Map<String, ProjectComponent> componentMap = ProjectProperty.getComponents(projectPath);
        List<String> requirements = ProjectProperty.getRequirements(projectPath);
        double fraction = 0.0;
        double step = 1.0 / requirements.size();
        String[] componentPath = StringUtils.splitByWholeSeparator(projectPath, File.separator);
        String subText = "Loading requirements for " + componentPath[componentPath.length - 1] + " - ";
        Map<String, String> packages = ProjectProperty.getPackages();
        for(String requirement: requirements){
            String [] pair = ProjectProperty.getRequirementNameAndVersion(requirement);
            if(!packages.containsKey(pair[0]) || VersionUtils.isVersionLower(packages.get(pair[0]), pair[1])) {
                indicator.setText2(subText + requirement);
                Runtime.getRuntime().exec("pip install " + requirement).waitFor();
            }
            fraction += step;
            indicator.setFraction(fraction);
        }
        indicator.setFraction(0.0);
        fraction = 0.0;
        step = 1.0 / componentMap.size();
        subText = "Loading components for " + componentPath[componentPath.length - 1] + " - ";
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
        String oldVersion = ProjectProperty.load(path.getPath(), ProjectProperty.VERSION_FILE).get(component.getName());
        if(oldVersion == null){
            return true;
        }
        oldVersion = ProjectProperty.getVersionBranch(oldVersion)[0];
        ProjectComponent oldComponent = new ProjectComponent(component.getName(), oldVersion, component.getLink(), component.getBranch());
        return StringUtils.isEmpty(component.getBranch()) || oldComponent.isLower(component);
    }
}
