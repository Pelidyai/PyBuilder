package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.publishing.CommonPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.LocalPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.PublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.ReleasePublishThread;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener{

    private final CommonPublishThread commonPublishThread;
    private final LocalPublishThread localPublishThread;
    private final ReleasePublishThread releasePublishThread;

    public PublishATMouseListener(JTree tree, Project project) {
        super(tree, project);
        this.commonPublishThread = new CommonPublishThread(project, "Publish process");
        this.localPublishThread = new LocalPublishThread(project, "Publish to local process");
        this.releasePublishThread = new ReleasePublishThread(project, "Publish release process");
    }

    @Override
    void runCommand(String command) {
        PublishThread publisher;
        super.runCommand(command);
        switch (command){
            case TreeCommands.PUBLISH:{
                publisher = commonPublishThread;
                break;
            }
            case TreeCommands.PUBLISH_RELEASE:{
                publisher = releasePublishThread;
                break;
            }
            case TreeCommands.PUBLISH_LOCAL:{
                publisher = localPublishThread;
                break;
            }
            default:{
                return;
            }
        }
        if (!publisher.isAlive()) {
            new ProgressManagerImpl().run(publisher);
        } else {
            Notifications.Bus.notify(new Notification("publish", publisher.getTitle(),
                    "Publishing is running", NotificationType.ERROR));
        }
    }
}
