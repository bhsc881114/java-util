package com.bhsc.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.bhsc.notify.QQMailNotify;

/**
 * 
 * @author bhsc881114
 *
 */
public class MailUtilTest {

  private static List<String> RECEIVERS = new ArrayList<String>();

  static {
    RECEIVERS.add("xxxx@163.com");
  }

  @Test
  public void sendTest() {
    QQMailNotify.SEND_MAIL = "xxxx@qq.com";
    QQMailNotify.SEND_PSWD = "xxxxxx";

    Assert.assertTrue(QQMailNotify.notify("hello", "xxx", RECEIVERS));
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
