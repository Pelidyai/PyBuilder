package com.pickaim.python_builder.win_factories;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Objects;

public class ToolbarView {

    private JButton refreshToolWindowButton;
    private final JPanel myToolWindowContent = new JPanel();

    static Icon scale(Icon icon, double scaleFactor) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        width = (int) Math.ceil(width * scaleFactor);
        height = (int) Math.ceil(height * scaleFactor);

        BufferedImage image =
                new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = image.createGraphics();
        g.scale(scaleFactor, scaleFactor);
        return new ImageIcon(image);
    }
    public ToolbarView(ToolWindow toolWindow) {
        DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(toolWindow.getProject().getName());
        DefaultMutableTreeNode buildTree = new DefaultMutableTreeNode("Build tool");
        buildTree.add(new DefaultMutableTreeNode("build"));
        treeRoot.add(buildTree);
        JTree tree = new JTree(treeRoot);
        DefaultTreeCellRenderer treeCellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();

/*        Font defaultFont = UIManager.getFont("Tree.font");
        Font currentFont = tree.getFont();

        double newScale = (double)
                defaultFont.getSize2D() / new ImageIcon(Objects.requireNonNull(ToolbarView.class.getResource("/imgs/ClosedPackage.png"))).getIconHeight();*/

        treeCellRenderer.setLeafIcon(new ImageIcon(Objects.requireNonNull(ToolbarView.class.getResource("/imgs/ClosedPackage2.png"))));
        treeCellRenderer.setClosedIcon(new ImageIcon(Objects.requireNonNull(ToolbarView.class.getResource("/imgs/ClosedPackage2.png"))));
        JButton hideToolWindowButton = new JButton("Build");
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        myToolWindowContent.setSize(100, 500);
        myToolWindowContent.add(hideToolWindowButton);
        myToolWindowContent.add(tree);
    }


    public JPanel getContent() {
        return myToolWindowContent;
    }

}
