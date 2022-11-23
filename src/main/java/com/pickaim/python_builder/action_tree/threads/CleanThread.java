package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class CleanThread extends AbstractBackgroundThread{
    public CleanThread(Project project, String title) {
        super(project, title);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Build");
        try {
            isAlive = true;
            Map<String, ProjectComponent> componentMap = ProjectProperty.getProjectComponents();
            for(String componentName: componentMap.keySet()){
                String p = ProjectProperty.getPythonDir() + File.separator + componentName;
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
}
