package com.pickaim.python_builder.action_tree;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.JBUI;
import com.jetbrains.python.inspections.PyInterpreterInspection;
import com.pickaim.python_builder.action_tree.listeners.TreeMouseActionListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.util.Arrays;
import com.intellij.ui.treeStructure.Tree;

public class TreeBuilder {
    public static JTree buildActionTree(Project project){
        SimpleTreeNode treeRoot = new SimpleTreeNode(project.getName());
        addBuildNode(treeRoot);
        Tree tree = new Tree(treeRoot);
        PyInterpreterInspection g = new PyInterpreterInspection();
        g.getMainToolId();
        System.out.println(g);
        tree.setBorder(JBUI.Borders.empty());
        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
        treeCellRenderer.setLeafIcon(AllIcons.Nodes.Services);
        treeCellRenderer.setClosedIcon(AllIcons.Nodes.ConfigFolder);
        treeCellRenderer.setOpenIcon(AllIcons.Actions.GeneratedFolder);

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
