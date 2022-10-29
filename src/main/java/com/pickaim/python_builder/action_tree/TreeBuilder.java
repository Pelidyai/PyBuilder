package com.pickaim.python_builder.action_tree;

import com.intellij.openapi.project.Project;
import com.intellij.util.ui.JBUI;
import com.pickaim.python_builder.action_tree.listeners.TreeMouseActionListener;
import com.pickaim.python_builder.icons.IconPath;
import com.pickaim.python_builder.utils.IconUtils;
import com.pickaim.python_builder.utils.PathUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Arrays;

public class TreeBuilder {
    public static JTree buildTreeFor(Project project){
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(project.getName());
        addBuildNode(treeRoot);
        JTree tree = new JTree(treeRoot);
        tree.setBorder(JBUI.Borders.empty());

        DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        treeCellRenderer.setLeafIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.LEAF_SCRIPT_ICON)));
        treeCellRenderer.setClosedIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.CLOSED_DIR_ICON)));
        treeCellRenderer.setOpenIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.OPENED_DIR_ICON)));

        System.out.println(Arrays.toString(tree.getActionMap().allKeys()));
        tree.addMouseListener(new TreeMouseActionListener(tree, project));
        tree.setCellRenderer(treeCellRenderer);

        return tree;
    }

    private static void addBuildNode(DefaultMutableTreeNode treeRoot){
        DefaultMutableTreeNode buildTree = new DefaultMutableTreeNode("Build tool");
        buildTree.add(new DefaultMutableTreeNode(TreeCommands.BUILD));
        treeRoot.add(buildTree);
    }
}
