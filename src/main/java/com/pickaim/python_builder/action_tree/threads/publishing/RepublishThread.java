package com.pickaim.python_builder.action_tree.threads.publishing;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RepublishThread extends PublishThread {

    public RepublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title, "Republish");
    }

    protected void publish() throws Exception {
        ProjectComponent currentComponent = ProjectProperty.getInstance(myProject).getCurrentComponent();
        if (currentComponent != null) {
            publishToBranch(currentComponent.getBranch(), currentComponent.getLink(), true);
        } else {
            throw new Exception("Cannot find project component publishing info");
        }
    }
}
