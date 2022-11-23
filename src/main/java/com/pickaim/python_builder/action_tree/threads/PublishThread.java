package com.pickaim.python_builder.action_tree.threads;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.utils.ProjectProperty;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        //TODO inspect remote branches
        Process process = Runtime.getRuntime().exec("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " && " + "git rev-parse --abbrev-ref HEAD"
        );
        String savedBranch = new String(process.getInputStream().readAllBytes());
        process.waitFor();
        process = Runtime.getRuntime().exec("cmd.exe /c cd /d \"" + projectPath + "\"" +
                " & " + "git add " + projectPath +
                " & " + "git commit -m \"Auto\"" +
                " & " + "git push" +
                " & " + "git switch -c " + branch +
                " && " + "git add " + projectPath +
                " && " + "git commit -m \"Auto\"" +
                " & " + "git push --force " + link +
                " & " + "git checkout " + savedBranch +
                " && " + "git branch --delete " + branch
        );
        process.waitFor();
    }
}
