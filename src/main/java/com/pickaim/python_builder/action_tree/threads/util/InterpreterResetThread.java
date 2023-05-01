package com.pickaim.python_builder.action_tree.threads.util;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.NotificationGroupID;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.Nullable;

public class InterpreterResetThread extends AbstractBackgroundThread {
    public InterpreterResetThread(@Nullable Project project) {
        super(project, "Interpreter reset", NotificationGroupID.UTIL_SETTINGS, "Interpreter reset");
    }

    @Override
    protected void doThreadAction(ProgressIndicator indicator) {
        ProjectProperty.getInstance(myProject).resetInterpreter();
    }
}
