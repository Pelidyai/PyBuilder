package com.pickaim.python_builder.action_tree.listeners;

import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;

public class UtilATMouseListener extends AbstractActionTreeMouseListener{

    public UtilATMouseListener(JTree tree) {
        super(tree);
    }

    @Override
    void runCommand(String command) {
        ProjectProperty.resolveComponents();
        switch (command){
            case TreeCommands.INTERPRETER:{
                ProjectProperty.resetInterpreter();
                break;
            }
            default:{
                break;
            }
        }
    }
}
