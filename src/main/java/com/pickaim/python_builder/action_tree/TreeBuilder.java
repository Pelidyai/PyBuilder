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
import com.intellij.ui.treeStructure.Tree;

public class TreeBuilder {
    public static JTree buildTreeFor(Project project){
        SimpleTreeNode treeRoot = new SimpleTreeNode(project.getName());
        addBuildNode(treeRoot);
        Tree tree = new Tree(treeRoot);
        tree.setBorder(JBUI.Borders.empty());
        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
        treeCellRenderer.setLeafIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.LEAF_SCRIPT_ICON)));
        treeCellRenderer.setClosedIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.CLOSED_DIR_ICON)));
        treeCellRenderer.setOpenIcon(IconUtils.getSVGIcon(PathUtils.getURLString(IconPath.OPENED_DIR_ICON)));

        System.out.println(Arrays.toString(tree.getActionMap().allKeys()));
        tree.addMouseListener(new TreeMouseActionListener(tree, project));
        tree.setVisible(true);
        tree.setCellRenderer(treeCellRenderer);

        return tree;
    }

    private static void addBuildNode(DefaultMutableTreeNode treeRoot){
        SimpleTreeNode buildTree = new SimpleTreeNode("Build tool");
        buildTree.add(new SimpleTreeNode(TreeCommands.BUILD));
        treeRoot.add(buildTree);
    }
}
