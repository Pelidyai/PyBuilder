package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommonPublishThread extends PublishThread{
    public CommonPublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Publish");
        try {
            isAlive = true;
            publish();
            isAlive = false;
            Notifications.Bus.notify(new Notification("publish", "Publish results",
                    "Publishing successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification("publish",
                    "Publish error", e.getMessage(), NotificationType.ERROR));
        }
    }

    private void publish() throws Exception {
        ProjectComponent currentComponent = ProjectProperty.getCurrentComponent();
        if (currentComponent != null) {
            publishToBranch(currentComponent.getBranch(), ProjectProperty.getNexusLink());
        } else {
            throw new Exception("Cannot find project component publishing info");
        }
    }

}
