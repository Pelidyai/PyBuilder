package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.publishing.CommonPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.LocalPublishThread;
import com.pickaim.python_builder.action_tree.threads.publishing.RepublishThread;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener {
    public PublishATMouseListener(JTree tree, Project project) {
        super(tree, project);
        commandToTask.put(TreeCommands.PUBLISH_LOCAL, new LocalPublishThread(project));
        commandToTask.put(TreeCommands.PUBLISH, new CommonPublishThread(project));
        commandToTask.put(TreeCommands.REPUBLISH, new RepublishThread(project));

        preActions.put(TreeCommands.REPUBLISH, new PreAction<>(() -> {
            int answer = Messages.showOkCancelDialog("Are you sure want to start republish process?",
                    "Republish", "Confirm", "Abort", Messages.getQuestionIcon());
            return answer == Messages.CANCEL ? PreAction.FAIL : PreAction.OK;
        }));
    }
}
