package com.bhsc.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * common env util，judge env,os...etc
 * 
 * @author bhsc881114
 *
 */
public class EnvUtil {
  final static Logger logger = LoggerFactory.getLogger(EnvUtil.class);

  static enum EnvType {
    ONLINE, PRE_PUB, DEV
  }

  private static String osName = "";
  private static String hostname = "localhost";
  private static String ip = "127.0.0.1";
  private static EnvType envType = EnvType.ONLINE;

  static {
    // TODO
    initEnv();

    initHostAddr();

    initOs();
  }

  public static boolean isOnline() {
    return envType == EnvType.ONLINE;
  }
  public static boolean isDev() {
    return envType == EnvType.DEV;
  }

  public static boolean isPrePub() {
    return envType == EnvType.PRE_PUB;
  }

  public static String getEnv() {
    return String.valueOf(envType);
  }

  public static boolean isWindows() {
    return osName != null && osName.toLowerCase().indexOf("windows") > -1;
  }

  public static boolean isLinux() {
    return osName != null && osName.toLowerCase().indexOf("linux") > -1;
  }

  public static String getHostname() {
    return hostname;
  }

  public static String getIp() {
    return ip;
  }

  private static void initEnv() {
    // TODO
  }

  private static void initOs() {
    osName = System.getProperty("os.name");
  }

  private static void initHostAddr() {
    try {
      InetAddress localhost = InetAddress.getLocalHost();
      hostname = localhost.getHostName();
      ip = localhost.getHostAddress();
    } catch (UnknownHostException e) {
      logger.error("initHostAddr." + e.getMessage(), e);
    }
  }
}
