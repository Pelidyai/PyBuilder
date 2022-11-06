package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CleanThread extends Task.Backgroundable{
    private boolean isAlive = false;

    public CleanThread(Project project, String title) {
        super(project, title, false);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Build");
        try {
            isAlive = true;
            for(ProjectComponent component: ProjectProperty.getProjectComponents()){
                String p = ProjectProperty.getPythonDir() + File.separator + component.getName();
                if(new File(p).exists()) {
                    FileUtils.deleteDirectory(new File(p));
                }
            }
            isAlive = false;
            Notifications.Bus.notify(new Notification("clean", "Clean results",
                    "Clean successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification("clean",
                    "Clean error", e.getMessage(), NotificationType.ERROR));
        }
    }

    public boolean isAlive() {
        return isAlive;
    }
}
