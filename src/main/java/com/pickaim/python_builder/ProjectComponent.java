package com.pickaim.python_builder;

import com.pickaim.python_builder.utils.VersionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProjectComponent {
    private final String name;
    private final String version;
    private final String link;
    private final String branch;

    public ProjectComponent(String name, String version, String link, String branch){
        this.name = name;
        this.version = version;
        this.link = link;
        this.branch = branch;
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

    public void cloning(){

    }

    public boolean isLower(ProjectComponent other){
        return VersionUtils.isVersionLower(this.version, other.getVersion());
    }

}
