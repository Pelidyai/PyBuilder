package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.BuildThread;
import com.pickaim.python_builder.action_tree.threads.CleanThread;

import javax.swing.*;

public class BuildATMouseListener extends AbstractActionTreeMouseListener {

    public BuildATMouseListener(JTree tree, Project project) {
        super(tree, project);
        commandToTask.put(TreeCommands.BUILD, new BuildThread(project, "Build process", "Project build"));
        commandToTask.put(TreeCommands.CLEAN, new CleanThread(project, "Clean process", "Dependencies clean"));
    }
}
