package com.bhsc.notify;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bhsc.json.GsonUtil;
import com.bhsc.log.LogUtil;

public class QQMailWorker implements Runnable {
  final Logger logger = LoggerFactory.getLogger(QQMailWorker.class);

  private String subject;
  private String content;
  private List<String> to;

  public QQMailWorker(String subject, String content, List<String> to) {
    super();
    this.subject = subject;
    this.content = content;
    this.to = to;
  }

  public void send() {
    if (null == to || to.size() < 1) {
      logger.error("receive list error" + subject + content + to);
      return;
    }
    long start = System.currentTimeMillis();
    Properties props = new Properties();

    if (QQMailNotify.SEND_MAIL.endsWith("@qq.com")
        || QQMailNotify.SEND_MAIL.endsWith("@vip.qq.com")
        || QQMailNotify.SEND_MAIL.endsWith("@foxmail.com")) {
      props.put("mail.smtp.host", "smtp.qq.com");
      props.put("mail.smtp.port", "25");
    } else {
      // 企业邮箱
      props.put("mail.smtp.host", "smtp.exmail.qq.com");
    }
    props.put("mail.smtp.auth", "true");

    Session sendMailSession = Session.getInstance(props, new Authenticator() {
      public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(QQMailNotify.SEND_MAIL, QQMailNotify.SEND_PSWD);
      }
    });

    try {
      Message newMessage = new MimeMessage(sendMailSession);
      newMessage.setFrom(new InternetAddress(QQMailNotify.SEND_MAIL));
      for (String des : to) {
        newMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(des));
      }
      newMessage.setSubject(subject);
      newMessage.setText(content);

      Transport.send(newMessage);
    } catch (Exception e) {
      LogUtil.error(logger, "send mail exception", subject, content, to, LogUtil.buildMessage(e));
    } finally {
      LogUtil.error(logger, "send mail", System.currentTimeMillis() - start, subject, content,
          GsonUtil.toJson(to));
    }
  }

  @Override
  public void run() {

    send();
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  public List<String> getTo() {
    return to;
  }
}
