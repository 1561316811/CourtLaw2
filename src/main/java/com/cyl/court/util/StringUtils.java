package com.cyl.court.util;

public class StringUtils {

    public static boolean isEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }
        return false;
    }


}
