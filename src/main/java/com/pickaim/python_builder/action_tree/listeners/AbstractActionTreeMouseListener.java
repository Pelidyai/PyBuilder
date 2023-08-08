package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class AbstractActionTreeMouseListener implements MouseListener {
    private final JTree tree;

    protected final Project project;
    protected final Map<String, AbstractBackgroundThread> commandToTask = new HashMap<>();
    protected final Map<String, PreAction<Object>> preActions = new HashMap<>();

    public AbstractActionTreeMouseListener(JTree tree, Project project) {
        this.tree = tree;
        this.project = project;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selRow != -1 && selPath != null) {
                if (e.getClickCount() == 2) {
                    runCommand(selPath.getLastPathComponent().toString());
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        // override if necessary
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        // override if necessary
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        // override if necessary
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        // override if necessary
    }

    private void updateProjectWithCommand() {
        ProjectProperty.getInstance(project).update();
        ProjectProperty.saveChanges();
    }

    void runCommand(String command) {
        PreAction<Object> preAction = preActions.get(command);
        if (preAction != null) {
            try {
                Object result = preAction.run();
                if (result == Integer.valueOf(PreAction.FAIL)) {
                    return;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        AbstractBackgroundThread commandTask = commandToTask.get(command);
        if (commandTask == null) {
            return;
        }
        updateProjectWithCommand();
        if (!commandTask.isAlive()) {
            new ProgressManagerImpl().run(commandTask);
        } else {
            Notifications.Bus.notify(new Notification(commandTask.getNotificationGroupID(), commandTask.getTitle(),
                    commandTask.getProcessName() + " is running", NotificationType.ERROR));
        }
    }
}
