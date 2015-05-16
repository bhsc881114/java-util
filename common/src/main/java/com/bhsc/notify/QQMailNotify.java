package com.bhsc.notify;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bhsc.common.EnvUtil;
import com.bhsc.log.LogUtil;

/**
 * QQ Mail NOtifyer
 * 
 * @author bhsc881114
 *
 */
public class QQMailNotify {
  final static Logger logger = LoggerFactory.getLogger(QQMailNotify.class);

  public static String SEND_MAIL = "xxx@xx.com";
  public static String SEND_PSWD = "xxxxxx";

  private static long ONE_MINUTE = 60 * 1000;
  private static final int MAX_NUM = 5;// 5 most mails one minute
  private static Map<String, Long> LAST_TIME = new ConcurrentHashMap<String, Long>();
  private static Map<String, AtomicInteger> COUNT = new ConcurrentHashMap<String, AtomicInteger>();

  private static ThreadPoolExecutor mailPool = new ThreadPoolExecutor(1, 5, 5, TimeUnit.MINUTES,
      new ArrayBlockingQueue<Runnable>(1000), new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
          QQMailWorker worker = (QQMailWorker) r;
          LogUtil.error(logger, "mail-pool-full", worker.getSubject(), worker.getContent(),
              worker.getTo());
        }
      });

  public static boolean notify(String subject, String content, List<String> to) {
    if (StringUtils.isBlank(subject)) {
      throw new RuntimeException("empty subject");
    }
    if (null == to || to.size() < 1) {
      throw new RuntimeException("no receivers");
    }
    if (!EnvUtil.isOnline()) {
      LogUtil.warn(logger, "not online env", subject, content, to);
      return false;
    }
    int controlCode = flowControl(subject);
    if (controlCode > 0) {
      if (controlCode > 1) {
        subject = subject + " " + controlCode + " time";
      }
      mailPool.execute(new QQMailWorker(subject, content, to));
      return true;
    }
    return false;
  }

  private static int flowControl(String subject) {
    AtomicInteger aic = COUNT.get(subject);
    if (aic == null) {
      synchronized (QQMailNotify.class) {
        if (aic == null) {
          aic = new AtomicInteger();
          COUNT.put(subject, aic);
        }
      }
    }
    aic.incrementAndGet();

    // subject last send time
    Long lastTime = LAST_TIME.get(subject);
    LAST_TIME.put(subject, System.currentTimeMillis());
    if (lastTime != null) {
      long div = (System.currentTimeMillis() - lastTime);
      if (div > ONE_MINUTE) {
        int count = aic.get();
        aic.set(0);
        return count;
      } else if (div < ONE_MINUTE && aic.get() > MAX_NUM) {
        return -1;
      }
    }
    return 1;
  }

}
