package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.publishing.CommonPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.LocalPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.ReleasePublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.RepublishThread;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener {
    public PublishATMouseListener(JTree tree, Project project) {
        super(tree, project);
        commandToTask.put(TreeCommands.PUBLISH_LOCAL, new LocalPublishThread(project, "Publish to local process"));
        commandToTask.put(TreeCommands.PUBLISH, new CommonPublishThread(project, "Publish process"));
        commandToTask.put(TreeCommands.REPUBLISH, new RepublishThread(project, "Republish process"));
        commandToTask.put(TreeCommands.PUBLISH_RELEASE, new ReleasePublishThread(project, "Publish release process"));
    }
}
