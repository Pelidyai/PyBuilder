package com.pickaim.python_builder.win_factories;

import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Objects;

public class TreeBuilder {
    public static JTree buildTreeFor(String projectName){
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(projectName);
        addBuildNode(treeRoot);
        JTree tree = new JTree(treeRoot);
        tree.setBorder(JBUI.Borders.empty());

        DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        treeCellRenderer.setLeafIcon(IconUtils.getSVGIcon(getURLString("/META-INF/pluginIcon.svg")));
        treeCellRenderer.setClosedIcon(new ImageIcon(Objects.requireNonNull(ToolbarView.class.getResource("/imgs/2.png"))));
        treeCellRenderer.setOpenIcon(new ImageIcon(Objects.requireNonNull(ToolbarView.class.getResource("/imgs/3.png"))));

        tree.setCellRenderer(treeCellRenderer);

        return tree;
    }


    private static void addBuildNode(DefaultMutableTreeNode treeRoot){
        DefaultMutableTreeNode buildTree = new DefaultMutableTreeNode("Build tool");
        buildTree.add(new DefaultMutableTreeNode("build"));
        treeRoot.add(buildTree);
    }
    private static String getURLString(String resourceName){
        return Objects.requireNonNull(TreeBuilder.class.getResource(resourceName)).toString();
    }
}
