package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.BuildThread;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;

public class BuildATMouseListener extends AbstractActionTreeMouseListener {
    private final BuildThread buildThread;

    public BuildATMouseListener(JTree tree, Project project){
        super(tree);
        this.buildThread = new BuildThread(project, "Build process");
    }

    @Override
    protected void runCommand(String command){
        switch (command){
            case TreeCommands.BUILD:{
                build();
                break;
            }
            case TreeCommands.CLEAN:{
                Messages.showInfoMessage("Clean", "Clean");
                break;
            }
            default:{
                break;
            }
        }
    }

    private void build(){
        if (!buildThread.isAlive()) {
            ProjectProperty.checkInterpreter();
            new ProgressManagerImpl().run(buildThread);
        } else {
            Notifications.Bus.notify(new Notification("build", "Build process",
                    "Build is running", NotificationType.ERROR));
        }
    }
}
