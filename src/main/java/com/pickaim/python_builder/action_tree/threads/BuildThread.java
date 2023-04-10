package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.NotificationGroupID;
import com.pickaim.python_builder.utils.ProjectBuilder;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.NotNull;

public class BuildThread extends AbstractBackgroundThread {
    public BuildThread(Project project, String title, String processName) {
        super(project, title, NotificationGroupID.BUILD, processName);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Build");
        ProjectProperty.getInstance(myProject).checkInterpreter();
        try {
            isAlive = true;
            ProjectBuilder.buildProject(ProjectProperty.getInstance(myProject).getProjectPath(),
                    indicator, myProject, ProjectProperty.getInstance(myProject).getProjectName());
            isAlive = false;
            Notifications.Bus.notify(new Notification(NotificationGroupID.BUILD, "Build results",
                    "Build successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification(NotificationGroupID.BUILD,
                    "Build error", e.getMessage(), NotificationType.ERROR));
        }
    }
}
