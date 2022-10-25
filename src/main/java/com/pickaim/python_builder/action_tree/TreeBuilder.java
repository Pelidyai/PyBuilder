package com.pickaim.python_builder.action_tree;

import com.intellij.util.ui.JBUI;
import com.pickaim.python_builder.action_tree.listeners.TreeMouseActionListener;
import com.pickaim.python_builder.icons.IconPath;
import com.pickaim.python_builder.icons.IconUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Arrays;
import java.util.Objects;

public class TreeBuilder {
    public static JTree buildTreeFor(String projectName){
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(projectName);
        addBuildNode(treeRoot);
        JTree tree = new JTree(treeRoot);
        tree.setBorder(JBUI.Borders.empty());

        DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        treeCellRenderer.setLeafIcon(IconUtils.getSVGIcon(getURLString(IconPath.LEAF_SCRIPT_ICON)));
        treeCellRenderer.setClosedIcon(IconUtils.getSVGIcon(getURLString(IconPath.CLOSED_DIR_ICON)));
        treeCellRenderer.setOpenIcon(IconUtils.getSVGIcon(getURLString(IconPath.OPENED_DIR_ICON)));
        System.out.println(Arrays.toString(tree.getActionMap().allKeys()));
        tree.addMouseListener(new TreeMouseActionListener(tree));
        tree.setCellRenderer(treeCellRenderer);

        return tree;
    }

    private static void addBuildNode(DefaultMutableTreeNode treeRoot){
        DefaultMutableTreeNode buildTree = new DefaultMutableTreeNode("Build tool");
        buildTree.add(new DefaultMutableTreeNode(TreeCommands.BUILD));
        treeRoot.add(buildTree);
    }
    private static String getURLString(String resourceName){
        return Objects.requireNonNull(TreeBuilder.class.getResource(resourceName)).toString();
    }
}
