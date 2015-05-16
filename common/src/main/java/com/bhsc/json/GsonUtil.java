package com.bhsc.json;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * easy json util
 * 
 * @author bhsc881114
 *
 */
public class GsonUtil {
  public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping()
      .setDateFormat(DEFAULT_DATE_FORMAT)
      .create();


  public static String toJson(Object obj, String dateFormat) {
    return gson.toJson(obj);
  }

  public static String toJson(Object obj) {
    return toJson(obj, DEFAULT_DATE_FORMAT);
  }

  public static <T> T fromJson(String json, Class<T> type, String dateFormat) {
    return gson.fromJson(json, type);
  }

  public static <T> T fromJson(String json, Class<T> type) {
    return fromJson(json, type, DEFAULT_DATE_FORMAT);
  }

  public static <T> T fromJson(String json, Type type) {
    return gson.fromJson(json, type);
  }


}
