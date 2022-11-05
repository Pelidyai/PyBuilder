package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.utils.ProjectBuilder;
import org.jetbrains.annotations.NotNull;

public class BuildThread extends Task.Backgroundable {
    private final String projectPath;
    private boolean isAlive = false;

    public BuildThread(Project project, String title) {
        super(project, title, false);
        this.projectPath = project.getBasePath();
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Build");
        try {
            isAlive = true;
            ProjectBuilder.buildProject(projectPath, indicator);
            isAlive = false;
            Notifications.Bus.notify(new Notification("build", "Build results",
                    "Build successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification("build", "Build error", e.getMessage(), NotificationType.ERROR));
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
}
