package com.pickaim.python_builder.action_tree.threads;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PublishThread extends AbstractBackgroundThread{
    public PublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {

    }
}
