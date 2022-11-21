package com.pickaim.python_builder;

import com.pickaim.python_builder.utils.ProjectProperty;
import com.pickaim.python_builder.utils.VersionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class ProjectComponent {
    private final String name;
    private final String version;
    private final String link;
    private final String branch;

    public ProjectComponent(String name, String version, String link, String branch) {
        this.name = name;
        this.version = version;
        if(StringUtils.isEmpty(link)){
            this.link = ProjectProperty.getNexusLink();
        } else {
            this.link = link;
        }
        if (branch.equals("release") || branch.equals("versions")) {
            if(!StringUtils.isEmpty(version)) {
                this.branch = branch + "/" + name + "/" + version;
            } else {
                this.branch = branch + "/" + name;
            }
        } else {
            this.branch = branch;
        }
    }

    public String getName(){
        return name;
    }

    public String getVersion(){
        return version;
    }

    public String getBranch() {
        return branch;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int hashCode(){
        return name.hashCode() + version.hashCode()
                + link.hashCode() + branch.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ProjectComponent))
            return false;
        ProjectComponent other = (ProjectComponent) obj;
        return other.getLink().equals(this.link)
                && other.getName().equals(this.name)
                && other.getBranch().equals(this.branch);
    }

    public void cloning() throws Exception{
        String command;
        if(StringUtils.isEmpty(branch)){
            command = "git clone" +
                    " " + link +
                    " " + ProjectProperty.getPythonDir() + File.separator + name;
        } else {
            command = "git clone --branch " + branch +
                    " " + link +
                    " " + ProjectProperty.getPythonDir() + File.separator + name;
        }
        Process process = Runtime.getRuntime().exec(command);
        int result = process.waitFor();
        if(result != 0){
            String errors = new String(process.getErrorStream().readAllBytes());
            throw new Exception(errors);
        }
    }

    public boolean isLower(ProjectComponent other){
        return VersionUtils.isVersionLower(this.version, other.getVersion());
    }

}
