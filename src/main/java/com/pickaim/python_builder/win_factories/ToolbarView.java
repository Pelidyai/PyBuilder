package com.pickaim.python_builder.win_factories;

import com.intellij.openapi.wm.ToolWindow;
import com.pickaim.python_builder.action_tree.TreeBuilder;

import javax.swing.*;
import java.awt.*;

public class ToolbarView {
    private final JPanel myToolWindowContent = new JPanel(new BorderLayout());
    public ToolbarView(ToolWindow toolWindow) {
        myToolWindowContent.setLocation(0,0);
        myToolWindowContent.add(TreeBuilder.buildTreeFor(toolWindow.getProject().getName()));
    }

    public JPanel getContent() {
        return myToolWindowContent;
    }
}
