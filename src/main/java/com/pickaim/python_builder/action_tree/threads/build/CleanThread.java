package com.pickaim.python_builder.action_tree.threads.build;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.pickaim.python_builder.NotificationGroupID;
import com.pickaim.python_builder.ProjectComponent;
import com.pickaim.python_builder.action_tree.threads.AbstractBackgroundThread;
import com.pickaim.python_builder.utils.ProjectProperty;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Map;

public class CleanThread extends AbstractBackgroundThread {
    public CleanThread(Project project, String title, String processName) {
        super(project, title, NotificationGroupID.CLEAN, processName);
    }

    @Override
    protected void doThreadAction(ProgressIndicator indicator) throws Exception {
        Map<String, ProjectComponent> componentMap = ProjectProperty.getInstance(myProject).getProjectComponents();
        for (String componentName : componentMap.keySet()) {
            String p = ProjectProperty.getInstance(myProject).getPythonDir() + File.separator + componentName;
            if (new File(p).exists()) {
                FileUtils.deleteDirectory(new File(p));
            }
        }
    }
}
