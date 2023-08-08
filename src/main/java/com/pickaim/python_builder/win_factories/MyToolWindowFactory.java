package com.pickaim.python_builder.win_factories;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.NotNull;


public class MyToolWindowFactory implements ToolWindowFactory {

    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        init(project);
        ToolbarView myToolWindow = new ToolbarView(toolWindow);
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void init(Project project) {
        ProjectProperty.getInstance(project).update();
    }

}
