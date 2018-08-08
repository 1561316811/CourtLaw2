package com.cyl.court.util;

import java.util.Objects;

public class StringUtils {

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }


    public static void requiredNoNullAndEmpty(String str){
        Objects.requireNonNull(str);
        if (StringUtils.isEmpty(str))
            throw new RuntimeException("str can not be empty!");
    }

}
