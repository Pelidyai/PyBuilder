package com.pickaim.python_builder.utils;

import com.pickaim.python_builder.action_tree.TreeBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathUtils {
    public static String getURLString(String resourceName){
        return Objects.requireNonNull(TreeBuilder.class.getResource(resourceName)).toString();
    }

    public static String getPythonPackagesPath(String pythonDir){
        String[] fullPathDirs =StringUtils.splitByWholeSeparator(pythonDir, "\\");
        List<String> pathList = (new ArrayList<>(List.of(fullPathDirs))).subList(0, fullPathDirs.length - 2);
        pathList.add("Lib");
        pathList.add("site-packages");
        pythonDir = StringUtils.join(pathList, '\\');
        return pythonDir;
    }
}
