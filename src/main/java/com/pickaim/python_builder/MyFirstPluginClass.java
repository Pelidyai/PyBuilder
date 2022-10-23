package com.pickaim.python_builder;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.ui.Messages;

public class MyFirstPluginClass extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Messages.showMessageDialog("Hoba", "not Hoba", Messages.getInformationIcon());
    }
}
