package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.BuildThread;
import com.pickaim.python_builder.action_tree.threads.CleanThread;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;

public class BuildATMouseListener extends AbstractActionTreeMouseListener {
    private final BuildThread buildThread;
    private final CleanThread cleanThread;

    public BuildATMouseListener(JTree tree, Project project){
        super(tree, project);
        this.buildThread = new BuildThread(project, "Build process");
        this.cleanThread = new CleanThread(project, "Clean process");
    }

    @Override
    protected void runCommand(String command){
        super.runCommand(command);
        switch (command){
            case TreeCommands.BUILD:{
                build();
                break;
            }
            case TreeCommands.CLEAN:{
                clean();
                break;
            }
            default:{
                break;
            }
        }
    }

    private void build(){
        if (!buildThread.isAlive()) {
            ProjectProperty.getInstance(buildThread.getProject()).checkInterpreter();
            new ProgressManagerImpl().run(buildThread);
        } else {
            Notifications.Bus.notify(new Notification("build", "Build process",
                    "Build is running", NotificationType.ERROR));
        }
    }

    private void clean(){
        if (!cleanThread.isAlive()) {
            ProjectProperty.getInstance(cleanThread.getProject()).checkInterpreter();
            new ProgressManagerImpl().run(cleanThread);
        } else {
            Notifications.Bus.notify(new Notification("clean", "Clean process",
                    "Clean is running", NotificationType.ERROR));
        }
    }
}
