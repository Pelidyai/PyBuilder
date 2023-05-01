package com.pickaim.python_builder.action_tree.threads.build;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.NotificationGroupID;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProjectBuilder;
import com.pickaim.python_builder.utils.ProjectProperty;

public class BuildThread extends AbstractBackgroundThread {
    public BuildThread(Project project, String title, String processName) {
        super(project, title, NotificationGroupID.BUILD, processName);
    }

    @Override
    protected void doThreadAction(ProgressIndicator indicator) throws Exception {
        ProjectBuilder.buildProject(ProjectProperty.getInstance(myProject).getProjectPath(),
                indicator, myProject, ProjectProperty.getInstance(myProject).getProjectName());
    }
}
