package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.util.GitTagCreationThread;
import com.pickaim.python_builder.action_tree.threads.util.InterpreterResetThread;

import javax.swing.*;

public class UtilATMouseListener extends AbstractActionTreeMouseListener {

    public UtilATMouseListener(JTree tree, Project project) {
        super(tree, project);
        commandToTask.put(TreeCommands.INTERPRETER, new InterpreterResetThread(project));
        commandToTask.put(TreeCommands.CREATE_TAG, new GitTagCreationThread(project));
    }
}
