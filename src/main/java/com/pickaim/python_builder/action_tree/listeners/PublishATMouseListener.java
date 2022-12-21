package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.ide.SaveAndSyncHandler;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.CommonPublishThread;
import com.pickaim.python_builder.action_tree.threads.LocalPublishThread;
import com.pickaim.python_builder.action_tree.threads.PublishThread;
import com.pickaim.python_builder.action_tree.threads.ReleasePublishThread;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;

public class PublishATMouseListener extends AbstractActionTreeMouseListener{

    private final CommonPublishThread commonPublishThread;
    private final LocalPublishThread localPublishThread;
    private final ReleasePublishThread releasePublishThread;

    public PublishATMouseListener(JTree tree, Project project) {
        super(tree);
        this.commonPublishThread = new CommonPublishThread(project, "Publish process");
        this.localPublishThread = new LocalPublishThread(project, "Publish to local process");
        this.releasePublishThread = new ReleasePublishThread(project, "Publish release process");
    }

    @Override
    void runCommand(String command) {
        PublishThread publisher;
        ProjectProperty.resolveComponents();
        FileDocumentManager documentManager = FileDocumentManager.getInstance();
        for(Document unsavedDoc: documentManager.getUnsavedDocuments()){
            documentManager.saveDocument(unsavedDoc);
        }
        SaveAndSyncHandler.getInstance().refreshOpenFiles();
        VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
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
