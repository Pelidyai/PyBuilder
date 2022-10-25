package com.pickaim.python_builder.utils;

import com.pickaim.python_builder.action_tree.TreeBuilder;

import java.util.Objects;

public class PathUtils {
    public static String getURLString(String resourceName){
        return Objects.requireNonNull(TreeBuilder.class.getResource(resourceName)).toString();
    }
}
