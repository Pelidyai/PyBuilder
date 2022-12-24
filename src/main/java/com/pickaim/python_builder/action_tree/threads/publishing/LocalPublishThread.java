package com.pickaim.python_builder.action_tree.threads.publishing;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class LocalPublishThread extends PublishThread{
    public LocalPublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title);
    }

    @Override
    protected void publish() throws Exception{
        String targetPath = ProjectProperty.getInstance(myProject).getPythonDir()
                + File.separator + ProjectProperty.getInstance(myProject).getProjectName();
        FileUtils.deleteDirectory(
                new File(targetPath)
        );
        FileUtils.copyDirectory(
                new File(ProjectProperty.getInstance(myProject).getProjectPath()),
                new File(targetPath)
        );
    }
}
