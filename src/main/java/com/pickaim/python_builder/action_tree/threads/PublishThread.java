package com.pickaim.python_builder.action_tree.threads;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
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
        // override if necessary
    }

    protected void publishToBranch(String branch, String link) throws Exception {
        String projectPath = ProjectProperty.getProjectPath();
        //TODO remote - commits, local - commits + changes
        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git remote add nexus " + ProjectProperty.getNexusLink() +
                " && " + "git remote update");
        String processOutput = ProcessRunner.runCommand("cmd.exe /c cd /d \"" +
                projectPath + "\"" +
                " && " + "git branch -r");
        Set<String> remoteBranches = Arrays.stream(StringUtils.splitByWholeSeparator(processOutput,"\n"))
                .filter(remoteBranch -> remoteBranch.startsWith("  nexus"))
                .map(String::trim)
                .collect(Collectors.toSet());
        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git remote remove nexus"
        );
        if(remoteBranches.contains("nexus/" + branch)){
            throw new Exception("Publishing to existing version.");
        }
        String savedBranch = ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git rev-parse --abbrev-ref HEAD"
        );
        ProcessRunner.runCommand("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " & " + "git switch -c " + branch +
                " & " + "git push --force " + link +
                " & " + "git checkout " + savedBranch +
                " && " + "git branch --delete " + branch
        );
    }
}
