package com.pickaim.python_builder.action_tree.listeners;

import com.pickaim.python_builder.utils.ProjectProperty;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class AbstractActionTreeMouseListener implements MouseListener {
    private final JTree tree;

    public AbstractActionTreeMouseListener(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
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
    public void mouseClicked(MouseEvent mouseEvent){
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

    void runCommand(String command){
        ProjectProperty.resolveComponents();
        ProjectProperty.saveChanges();
    }
}
