package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.ui.Messages;
import com.pickaim.python_builder.action_tree.TreeCommands;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener{

    public PublishATMouseListener(JTree tree) {
        super(tree);
    }

    @Override
    void runCommand(String command) {
        switch (command){
            case TreeCommands.PUBLISH:{
                Messages.showInfoMessage("Publish", "Publish");
                break;
            }
            default:{
                break;
            }
        }
    }
}
