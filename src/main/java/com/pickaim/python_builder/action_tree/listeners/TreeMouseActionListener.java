package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.progress.impl.ProgressManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.pickaim.python_builder.action_tree.threads.BuildThread;
import com.pickaim.python_builder.utils.PathUtils;
import com.pickaim.python_builder.utils.ProjectBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

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
        try {
            switch (command) {
                case TreeCommands.BUILD: {
                    if (!buildThread.isAlive()) {
                        Process process = Runtime.getRuntime().exec("where python");
                        String input = new String(process.getInputStream().readAllBytes());
                        String[] pythonDirs = StringUtils.splitByWholeSeparator(input, "\r\n");
                        if (pythonDirs.length < 1) {
                            Messages.showErrorDialog("Python interpreter not found", "Python Interpreter Problem");
                            return;
                        }
                        String pythonDir = pythonDirs[0];
                        if (pythonDirs.length > 1) {
                            pythonDir = Messages.showEditableChooseDialog("Select interpreter", "Interpreter Selecting",
                                    Messages.getQuestionIcon(), pythonDirs, pythonDirs[0], new NonEmptyInputValidator());
                        }
                        ProjectBuilder.setPythonDir(PathUtils.getPythonPackagesPath(pythonDir));
                        new ProgressManagerImpl().run(buildThread);
//                        new CoreProgressManager().run(buildThread);
                    } else {
                        Messages.showInfoMessage("Build is running", "Build Results");
                    }
                    break;
                }
                default: {

                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
