package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.build.BuildThread;
import com.pickaim.python_builder.action_tree.threads.build.CleanThread;

import javax.swing.*;

public class BuildATMouseListener extends AbstractActionTreeMouseListener {

    public BuildATMouseListener(JTree tree, Project project) {
        super(tree, project);
        commandToTask.put(TreeCommands.BUILD, new BuildThread(project));
        commandToTask.put(TreeCommands.CLEAN, new CleanThread(project));
    }
}
