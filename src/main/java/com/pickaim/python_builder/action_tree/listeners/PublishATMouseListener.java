package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.PublishThread;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener{

    private PublishThread publishThread;

    public PublishATMouseListener(JTree tree, Project project) {
        super(tree);
        this.publishThread = new PublishThread(project, "Publish process");
    }

    @Override
    void runCommand(String command) {
        switch (command){
            case TreeCommands.PUBLISH:{
                new ProgressManagerImpl().run(publishThread);
                break;
            }
            default:{
                break;
            }
        }
    }
}
