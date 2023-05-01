package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class AbstractBackgroundThread extends Task.Backgroundable {
    protected boolean isAlive = false;
    protected final String notificationGroupID;
    protected final String processName;

    public AbstractBackgroundThread(@Nullable Project project, @NotNull String title, String notificationGroupID, String processName) {
        super(project, title, false);
        this.notificationGroupID = notificationGroupID;
        this.processName = processName;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getNotificationGroupID() {
        return this.notificationGroupID;
    }

    public String getProcessName() {
        return processName;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText(getTitle());
        try {
            isAlive = true;
            doThreadAction(indicator);
            isAlive = false;
            Notifications.Bus.notify(new Notification(this.getNotificationGroupID(), this.getProcessName() + " results",
                    this.getProcessName() + " successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification(this.getNotificationGroupID(),
                    this.getProcessName() + " error", e.getMessage(), NotificationType.ERROR));
        }
    }

    abstract protected void doThreadAction(ProgressIndicator indicator) throws Exception;
}
