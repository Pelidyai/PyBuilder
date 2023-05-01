package com.pickaim.python_builder.action_tree.threads.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.NotificationGroupID;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProcessRunner;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.Nullable;

public class GitTagCreationThread extends AbstractBackgroundThread {
    public GitTagCreationThread(@Nullable Project project) {
        super(project, "Git tag creation", NotificationGroupID.UTIL_SETTINGS, "Git tag creation");
    }

    @Override
    protected void doThreadAction(ProgressIndicator indicator) throws Exception {
        String projectPath = ProjectProperty.getInstance(myProject).getProjectPath();
        String tagName = "v." + ProjectProperty.getInstance(myProject).getCurrentComponent().getVersion();
        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git tag -a " + tagName + " -b \"" + tagName + "\"" +
                " && " + "git push origin " + tagName);
    }
}
