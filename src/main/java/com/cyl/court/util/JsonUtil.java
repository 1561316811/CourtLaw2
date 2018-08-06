package com.cyl.court.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtil {

	public static Gson gsonH = new GsonBuilder()
			.serializeNulls()
			.disableHtmlEscaping().create();
	
	/**
	 * replaceAll("null", "\"\"")
	 * @param obj
	 * @return
	 */
	public static String toJsonDisableHtmlEscaping(Object obj){
		return gsonH.toJson(obj);
	}
	
	public static <T> T fromJson(Type type, String msg) {
		return gsonH.fromJson(msg, type);
	}

}
