package com.pickaim.python_builder.action_tree;

import com.intellij.icons.AllIcons;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.openapi.project.Project;

import com.pickaim.python_builder.action_tree.listeners.BuildATMouseListener;
import com.pickaim.python_builder.action_tree.listeners.PublishATMouseListener;
import com.pickaim.python_builder.action_tree.listeners.UtilATMouseListener;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


public class TreeBuilder {
    public static JTree buildActionTree(Project project){
        SimpleTreeNode treeRoot = new SimpleTreeNode(project.getName());
        addBuildNode(treeRoot);
        addPublishNode(treeRoot);
        addUtilNode(treeRoot);
        Tree tree = new Tree(treeRoot);

        DefaultTreeCellRenderer treeCellRenderer = new DefaultTreeCellRenderer();
        treeCellRenderer.setLeafIcon(AllIcons.Nodes.Services);
        treeCellRenderer.setClosedIcon(AllIcons.Nodes.ConfigFolder);
        treeCellRenderer.setOpenIcon(AllIcons.Actions.GeneratedFolder);

        tree.addMouseListener(new BuildATMouseListener(tree, project));
        tree.addMouseListener(new PublishATMouseListener(tree, project));
        tree.addMouseListener(new UtilATMouseListener(tree, project));
        tree.setVisible(true);
        tree.setCellRenderer(treeCellRenderer);

        return tree;
    }

    private static void addBuildNode(DefaultMutableTreeNode treeRoot){
        SimpleTreeNode buildTree = new SimpleTreeNode("build");
        buildTree.add(new SimpleTreeNode(TreeCommands.BUILD));
        buildTree.add(new SimpleTreeNode(TreeCommands.CLEAN));
        treeRoot.add(buildTree);
    }

    private static void addPublishNode(DefaultMutableTreeNode treeRoot){
        SimpleTreeNode publishTree = new SimpleTreeNode("publishing");
        publishTree.add(new SimpleTreeNode(TreeCommands.PUBLISH));
        publishTree.add(new SimpleTreeNode(TreeCommands.PUBLISH_LOCAL));
        publishTree.add(new SimpleTreeNode(TreeCommands.PUBLISH_RELEASE));
        treeRoot.add(publishTree);
    }

    private static void addUtilNode(DefaultMutableTreeNode treeRoot){
        SimpleTreeNode utilTree = new SimpleTreeNode("utils");
        utilTree.add(new SimpleTreeNode(TreeCommands.INTERPRETER));
        treeRoot.add(utilTree);
    }
}
