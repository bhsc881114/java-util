package com.bhsc.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;

/**
 * easy log util
 * 
 * @author bhsc881114
 *
 */
public class LogUtil {

  private static final String split = ",";


  public static void info(Logger log, Object... args) {
    log.info(buildMessageWithSplit(split, args));
  }

  public static void warn(Logger log, Object... args) {
    log.warn(buildMessageWithSplit(split, args));
  }

  public static void error(Logger log, Object... args) {
    log.info(buildMessageWithSplit(split, args));
  }

  public static String buildMessage(Throwable e) {
    if (null == e) {
      return "";
    }
    String message = "undefine";
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    try {
      e.printStackTrace(new PrintWriter(buf, true));
      message = buf.toString();
    } finally {
      try {
        buf.close();
      } catch (IOException ex) {
      }
    }
    return message;
  }

  public static String buildMessage(Object... args) {
    return buildMessageWithSplit(split, args);
  }

  public static String buildMessageWithSplit(String split, Object... args) {
    if (args.length < 1) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      builder.append(String.valueOf(args[i]));
      if (i + 1 < args.length) {
        builder.append(split);
      }
    }
    return builder.toString();
  }
}
