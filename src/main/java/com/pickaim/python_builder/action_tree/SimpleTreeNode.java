package com.pickaim.python_builder.action_tree;

import com.intellij.openapi.vcs.update.AbstractTreeNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class SimpleTreeNode extends AbstractTreeNode {
    private final String name;

    public SimpleTreeNode(String name){
        this.name = name;
    }

    @Override
    protected @Nls @NotNull String getName() {
        return name;
    }

    @Override
    protected int getItemsCount() {
        return this.children.size();
    }

    @Override
    protected boolean showStatistics() {
        return false;
    }

    @Override
    public @NonNls Icon getIcon(boolean expanded) {
        return null;
    }

    @Override
    public @NotNull Collection<VirtualFile> getVirtualFiles() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull Collection<File> getFiles() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull SimpleTextAttributes getAttributes() {
        return this.myFilterAttributes;
    }

    @Override
    public boolean getSupportsDeletion() {
        return false;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
