package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.CommonPublishThread;
import com.pickaim.python_builder.action_tree.threads.PublishThread;
import com.pickaim.python_builder.action_tree.threads.ReleasePublishThread;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener{

    private final CommonPublishThread commonPublishThread;
    private final ReleasePublishThread releasePublishThread;

    public PublishATMouseListener(JTree tree, Project project) {
        super(tree);
        this.commonPublishThread = new CommonPublishThread(project, "Publish process");
        this.releasePublishThread = new ReleasePublishThread(project, "Publish process");
    }

    @Override
    void runCommand(String command) {
        PublishThread publisher;
        switch (command){
            case TreeCommands.PUBLISH:{
                publisher = commonPublishThread;
                break;
            }
            case TreeCommands.PUBLISH_RELEASE:{
                publisher = releasePublishThread;
                break;
            }
            default:{
                return;
            }
        }
        if (!publisher.isAlive()) {
            new ProgressManagerImpl().run(publisher);
        } else {
            Notifications.Bus.notify(new Notification("publish", "Publish process",
                    "Publishing is running", NotificationType.ERROR));
        }
    }
}
