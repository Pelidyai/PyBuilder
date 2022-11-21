package com.pickaim.python_builder.action_tree.threads;

import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class AbstractBackgroundThread extends Task.Backgroundable{

    public boolean isAlive = false;

    public AbstractBackgroundThread(@Nullable Project project, @NotNull String title) {
        super(project, title, false);
    }

    public boolean isAlive() {
        return isAlive;
    }
}
