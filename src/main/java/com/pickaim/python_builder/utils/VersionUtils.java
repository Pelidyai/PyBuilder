package com.pickaim.python_builder.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class VersionUtils {
    public static boolean isVersionLower(String oldV, String newV){
        List<Integer> oldInt =  getVersionInts(oldV);
        List<Integer> newInt =  getVersionInts(newV);
        if(oldInt.size() == newInt.size()){
            for(int i = 0; i < oldInt.size(); i++){
                if(newInt.get(i) > oldInt.get(i)){
                    return true;
                }
            }
            return false;
        } else return oldInt.size() < newInt.size();
    }

    private static List<Integer> getVersionInts(String version){
        List<Integer> result = new ArrayList<>();
        List.of(StringUtils.splitByWholeSeparator(version,".")).forEach(e -> result.add(Integer.valueOf(e)));
        return result;
    }
}
