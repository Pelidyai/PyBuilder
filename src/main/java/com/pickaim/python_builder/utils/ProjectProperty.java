package com.pickaim.python_builder.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.NonEmptyInputValidator;
import com.jetbrains.python.sdk.PythonSdkUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Objects;

public class ProjectProperty {
    
    private static String pythonDir = "";

    public static String getPythonDir(){
        return pythonDir;
    }

    public static void checkInterpreter(){
        if(StringUtils.isEmpty(pythonDir)) {
           resetInterpreter();
        }
    }

    public static void resetInterpreter(){
        pythonDir = getSelectedInterpreter();
        Notifications.Bus.notify(new Notification("util-settings", "Interpreter setting",
                "Interpreter was set to " + pythonDir, NotificationType.INFORMATION));
    }

    private static String getSelectedInterpreter(){
        List<Sdk> pythonSdks = PythonSdkUtil.getAllSdks();
        if (pythonSdks.isEmpty()) {
            Messages.showErrorDialog("Python interpreter not found", "Python Interpreter Problem");
            return "";
        }
        int sdkIdx = 0;
        if (pythonSdks.size() > 1) {
            String[] variants = new String[pythonSdks.size()];
            for (int i = 0; i < pythonSdks.size(); i++) {
                variants[i] = pythonSdks.get(i).getHomePath();
            }
            String selected = Messages.showEditableChooseDialog("Select interpreter", "Interpreter Selecting",
                    Messages.getQuestionIcon(), variants, variants[0], new NonEmptyInputValidator());
            sdkIdx = List.of(variants).indexOf(selected);
        }
        return Objects.requireNonNull(PythonSdkUtil.getSitePackagesDirectory(pythonSdks.get(sdkIdx))).getPath();
    }
}
