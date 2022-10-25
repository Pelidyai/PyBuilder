package com.pickaim.python_builder.action_tree.listeners;

import com.intellij.openapi.ui.Messages;
import com.pickaim.python_builder.action_tree.TreeCommands;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TreeMouseActionListener implements MouseListener {
    private final JTree tree;
    public TreeMouseActionListener(JTree tree){
        this.tree = tree;
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


    private void runCommand(TreePath path){
        String command = path.getLastPathComponent().toString();
        switch(command){
            case TreeCommands.BUILD:{
                Messages.showMessageDialog("BuildScript", "Dialog", Messages.getInformationIcon());
                break;
            }
            default:{

                break;
            }
        }
    }
}
