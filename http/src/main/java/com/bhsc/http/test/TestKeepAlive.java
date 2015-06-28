package com.bhsc.http.test;

import java.io.IOException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bhsc.http.HttpPostUtil;

/**
 * test http keepalive
 * 
 * @author chentao
 *
 */
public class TestKeepAlive {

  protected static final Logger logger = LoggerFactory.getLogger(TestKeepAlive.class);

  private static final int THREADS = 2;
  private static final String TEST_URL = "http://yunpian.com/v1/sms/send.json";
  private static final String TEST_HTTPS_URL = "https://yunpian.com/v1/sms/send.json";

  public static void main(String[] args) throws InterruptedException {
    DOMConfigurator.configure("src/main/java/test-log4j.xml");
    testShort(TEST_URL);
    testKeep(TEST_URL);

    testShort(TEST_HTTPS_URL);
    testKeep(TEST_HTTPS_URL);
    Thread.sleep(4 * 60 * 60 * 1000);
  }

  public static void testShort(final String url) {
    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread() {
        public void run() {
          while (true) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try {
              long start = System.currentTimeMillis();
              String resp = HttpPostUtil.requestString(url, null, httpclient);
              logger.warn("short," + url + "," + (System.currentTimeMillis() - start) + "," + resp);
              Thread.sleep(500);
            } catch (Exception e) {
              logger.warn("", e);
            } finally {
              try {
                httpclient.close();
              } catch (IOException e) {
                logger.warn("", e);
              }
            }
          }
        }
      };
      thread.start();
    }
  }

  public static void testKeep(final String url) {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    // cm.setMaxTotal(200);
    // cm.setDefaultMaxPerRoute(20);
    final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread() {
        public void run() {
          while (true) {
            try {
              long start = System.currentTimeMillis();
              String resp = HttpPostUtil.requestString(url, null, httpClient);
              logger.info("keep," + url + "," + (System.currentTimeMillis() - start) + "," + resp);
              Thread.sleep(500);
            } catch (Exception e) {
              logger.warn("", e);
            }
          }
        }
      };
      thread.start();
    }
  }

}
