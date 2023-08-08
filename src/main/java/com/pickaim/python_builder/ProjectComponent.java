package com.pickaim.python_builder;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.utils.ProcessRunner;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

public class ProjectComponent {
    private final String name;
    private final String version;
    private final String link;
    private final String branch;
    private final Project project;

    public ProjectComponent(String name, String version, String link, Project project) {
        this.name = name;
        this.version = version;
        this.project = project;
        if (StringUtils.isEmpty(link)) {
            this.link = ProjectProperty.getInstance(project).getNexusLink();
            if (!StringUtils.isEmpty(version)) {
                this.branch = name + "/" + version;
            } else {
                this.branch = "";
            }
        } else {
            this.link = link;
            this.branch = "";
        }
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getBranch() {
        return branch;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + version.hashCode()
                + link.hashCode() + branch.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ProjectComponent other)) {
            return false;
        }
        return other.getLink().equals(this.link)
                && other.getName().equals(this.name)
                && other.getBranch().equals(this.branch);
    }

    public void cloning() throws Exception {
        String command;
        if (StringUtils.isEmpty(branch)) {
            command = "git clone" +
                    " " + link +
                    " " + ProjectProperty.getInstance(project).getPythonDir() + File.separator + name;
        } else {
            command = "git clone --branch " + branch +
                    " " + link +
                    " " + ProjectProperty.getInstance(project).getPythonDir() + File.separator + name;
        }
        ProcessRunner.runCommand(command);
        File innerSrcDir = new File(ProjectProperty.getInstance(project).getPythonDir() +
                File.separator + name +
                File.separator + name);
        FileUtils.copyDirectory(
                innerSrcDir,
                new File(ProjectProperty.getInstance(project).getPythonDir() + File.separator + name)
        );
        FileUtils.deleteDirectory(innerSrcDir);
    }

    public boolean isNeedClone(ProjectComponent other) {
        Map<String, ProjectComponent> projectComponentMap = ProjectProperty.getInstance(project).getProjectComponents();
        ProjectComponent hardcodedComponent = projectComponentMap.get(this.getName());
        if (hardcodedComponent == null) {
            return !this.version.equals(other.getVersion());
        }
        if (this.version.equals(hardcodedComponent.version)) {
            return false;
        }
        return !this.version.equals(other.getVersion());
    }
}
