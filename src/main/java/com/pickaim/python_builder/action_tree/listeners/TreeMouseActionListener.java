package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.intellij.terminal.TerminalShellCommandHandler;
import com.pickaim.python_builder.action_tree.TreeCommands;
import com.intellij.terminal.TerminalExecutionConsole;
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

    private final String projectPath;

    public TreeMouseActionListener(JTree tree, String projectPath) {
        this.tree = tree;
        this.projectPath = projectPath;
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
                    Process process = Runtime.getRuntime().exec("where python");
                    String input = new String(process.getInputStream().readAllBytes());
                    String[] pythonDirs = StringUtils.splitByWholeSeparator(input, "\r\n");
                    if(pythonDirs.length < 1){
                        Messages.showErrorDialog("Python interpreter not found", "Python Interpreter Problem");
                        return;
                    }
                    String pythonDir = pythonDirs[0];
                    /*if(pythonDirs.length > 1) {
                        pythonDir = Messages.showEditableChooseDialog("Select interpreter", "Interpreter Selecting",
                                Messages.getQuestionIcon(), pythonDirs, pythonDirs[0], new NonEmptyInputValidator());
                    }*/
                    pythonDir = PathUtils.getPythonPackagesPath(pythonDir);
                    try {
                        ProjectBuilder.buildProject(projectPath, pythonDir);
                    } catch (Exception e) {
                        Messages.showErrorDialog(e.getMessage(), "Build Error");
                    }
                    System.out.println(pythonDir);
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
