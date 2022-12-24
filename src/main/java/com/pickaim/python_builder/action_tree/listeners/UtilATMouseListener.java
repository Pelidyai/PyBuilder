package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;

public class UtilATMouseListener extends AbstractActionTreeMouseListener{

    public UtilATMouseListener(JTree tree, Project project) {
        super(tree, project);
    }

    @Override
    void runCommand(String command) {
        super.runCommand(command);
        switch (command){
            case TreeCommands.INTERPRETER:{
                ProjectProperty.getInstance(project).resetInterpreter();
                break;
            }
            default:{
                break;
            }
        }
    }
}
