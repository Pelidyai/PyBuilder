package com.pickaim.python_builder.action_tree.threads;

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
}
