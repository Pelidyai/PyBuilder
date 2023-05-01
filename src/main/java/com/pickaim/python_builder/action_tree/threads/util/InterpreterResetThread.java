package com.pickaim.python_builder.action_tree.threads.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.Nullable;

public class InterpreterResetThread extends AbstractBackgroundThread {
    public InterpreterResetThread(@Nullable Project project, String notificationGroupID) {
        super(project, "Interpreter reset", notificationGroupID, "Interpreter reset");
    }

    @Override
    protected void doThreadAction(ProgressIndicator indicator) {
        ProjectProperty.getInstance(myProject).resetInterpreter();
    }
}
