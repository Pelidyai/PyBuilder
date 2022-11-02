package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.python.sdk.*;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.BuildThread;
import com.pickaim.python_builder.utils.ProjectBuilder;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Objects;

public class TreeMouseActionListener implements MouseListener {
    private final JTree tree;

    private final BuildThread buildThread;

    private final Project project;

    public TreeMouseActionListener(JTree tree, Project project) {
        this.tree = tree;
        this.project = project;
        this.buildThread = new BuildThread(project, "Build process");
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1 && selPath != null) {
            if (e.getClickCount() == 2) {
                runCommand(selPath);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }


    private void runCommand(TreePath path) {
        String command = path.getLastPathComponent().toString();
        switch (command) {
            case TreeCommands.BUILD: {
                if (!buildThread.isAlive()) {
                    List<Sdk> pythonSdks = PythonSdkUtil.getAllSdks();
                    if (pythonSdks.isEmpty()) {
                        Messages.showErrorDialog("Python interpreter not found", "Python Interpreter Problem");
                        return;
                    }
                    int sdkIdx = 0;
                    if (pythonSdks.size() > 1) {
                        String[] variants = new String[pythonSdks.size()];
                        for (int i = 0; i < pythonSdks.size(); i++) {
                            variants[i] = pythonSdks.get(i).getHomePath();
                        }
                        String selected = Messages.showEditableChooseDialog("Select interpreter", "Interpreter Selecting",
                                Messages.getQuestionIcon(), variants, variants[0], new NonEmptyInputValidator());
                        sdkIdx = List.of(variants).indexOf(selected);
                    }
                    VirtualFile pythonDir = Objects.requireNonNull(PythonSdkUtil.getSitePackagesDirectory(pythonSdks.get(sdkIdx)));
                    ProjectBuilder.setPythonDir(pythonDir.getPath());
                    new ProgressManagerImpl().run(buildThread);
                } else {
                    Messages.showInfoMessage("Build is running", "Build Results");
                }
                break;
            }
            default: {

                break;
            }
        }
    }
}
