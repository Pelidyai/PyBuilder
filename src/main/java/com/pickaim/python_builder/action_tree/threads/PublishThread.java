package com.pickaim.python_builder.action_tree.threads;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.utils.ProjectProperty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PublishThread extends AbstractBackgroundThread{
    public PublishThread(@Nullable Project project, @NotNull String title) {
        super(project, title);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Publish");
        try {
            isAlive = true;
            publish();
            isAlive = false;
            Notifications.Bus.notify(new Notification("publish", "Publish results",
                    "Publishing successful", NotificationType.INFORMATION));
        } catch (Exception e) {
            isAlive = false;
            Notifications.Bus.notify(new Notification("publish",
                    "Publish error", e.getMessage(), NotificationType.ERROR));
        }
    }

    private void publish() throws Exception{
        ProjectComponent currentComponent = ProjectProperty.getCurrentComponent();
        if(currentComponent != null) {
            Process proc = Runtime.getRuntime().exec("cmd.exe /c cd /d \"" + getProject().getBasePath() + "\"" +
                    " && " + "git rev-parse --abbrev-ref HEAD"
            );
            String savedBranch = new String(proc.getInputStream().readAllBytes());
            proc.waitFor();
            proc = Runtime.getRuntime().exec("cmd.exe /c cd /d \"" + getProject().getBasePath() + "\"" +
                    " & " + "git add " + getProject().getBasePath() +
                    " & " + "git commit -m \"Auto\"" +
                    " & " + "git push" +
                    " & " + "git switch -c " + currentComponent.getBranch()+
                    " && " + "git add " + getProject().getBasePath() +
                    " && " + "git commit -m \"Auto\"" +
                    " & " + "git push --force " + ProjectProperty.getNexusLink() +
                    " & " + "git checkout " + savedBranch +
                    " && " + "git branch --delete " + currentComponent.getBranch()
            );
            proc.waitFor();
        } else {
            throw new Exception("Cannot find project component info");
        }
    }
}
