package com.pickaim.python_builder.action_tree.threads.publishing;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProcessRunner;
import com.pickaim.python_builder.utils.ProjectProperty;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PublishThread extends AbstractBackgroundThread {
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

    protected void publish() throws Exception{
        //Override if necessary
    }

    protected void publishToBranch(String branch, String link) throws Exception {
        String projectPath = ProjectProperty.getProjectPath();
        //TODO remote - commits, local - commits + changes
        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git remote add nexus " + ProjectProperty.getNexusLink() +
                " && " + "git remote update");
        String savedBranch = ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git rev-parse --abbrev-ref HEAD"
        );
        String processOutput = ProcessRunner.runCommand("cmd.exe /c cd /d \"" +
                projectPath + "\"" +
                " && " + "git branch -r");
        Set<String> remoteBranches = Arrays.stream(StringUtils.splitByWholeSeparator(processOutput, "\n"))
                .filter(remoteBranch -> remoteBranch.startsWith("  nexus"))
                .map(String::trim)
                .collect(Collectors.toSet());
        if (remoteBranches.contains("nexus/" + branch)) {
            ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                    " && " + "git remote remove nexus"
            );
            throw new Exception("Publishing to existing version.");
        }

        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " & " + "git stash push" +
                " && " + "git switch -c " + branch + " nexus/master" +
                " && " + "git checkout " + savedBranch + " ." +
                " && " + "git add ." +
                " && " + "git commit -m \"Publishing\"" +
                " && " + "git push --force " + link +
                " & " + "git checkout -f " + savedBranch +
                " & " + "git branch -D " + branch +
                " & " + "git stash pop"
        );

        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git remote remove nexus"
        );
    }
}
