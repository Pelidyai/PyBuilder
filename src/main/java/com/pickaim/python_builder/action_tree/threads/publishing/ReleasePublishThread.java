package com.pickaim.python_builder.action_tree.threads.publishing;

import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReleasePublishThread extends PublishThread{
    public ReleasePublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title);
    }

    @Override
    protected void publish() throws Exception {
        ProjectComponent currentComponent = ProjectProperty.getInstance(myProject).getCurrentComponent();
        if (currentComponent != null) {
            publishToBranch(currentComponent.getReleaseBranch(), currentComponent.getLink());
        } else {
            throw new Exception("Cannot find project component publishing info");
        }
    }
}
