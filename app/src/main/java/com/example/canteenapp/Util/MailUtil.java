package com.example.canteenapp.Util;

import static com.example.canteenapp.constant.EmailConstant.FROM;
import static com.example.canteenapp.constant.EmailConstant.PASSWORD;
import static com.example.canteenapp.constant.EmailConstant.USERNAME;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailUtil {
  //  public static void send(String to, String sub, String msg) {
//    //Get properties object
//    Properties props = new Properties();
//
//    //get Session
//    Session session = Session.getDefaultInstance(props,
//            new javax.mail.Authenticator() {
//              protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("canteendigital@gmail.com", "Ashish123");
//              }
//            });
//    //compose message
//    try {
//      MimeMessage message = new MimeMessage(session);
//      message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//      message.setSubject(sub);
//      message.setText(msg);
//      //send message
//      Transport.send(message);
//      System.out.println("message sent successfully");
//    } catch (MessagingException e) {
//      throw new RuntimeException(e);
//    }
//
//  }
  public static void send(String to, String sub, String msg) {
    String stringHost = "smtp.gmail.com";
    Properties properties = System.getProperties();
    properties.put("mail.smtp.host", stringHost);
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");

    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(FROM));
      message.setRecipients(Message.RecipientType.TO,
              InternetAddress.parse(to));
      message.setSubject(sub);
      message.setText(msg);
      Transport.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }
//  public static void sendEmail(String to, String sub, String msg) throws EmailException, EmailException {
//    Email email = new SimpleEmail();
//    email.setHostName(GOOGLE_SMTP_URL);
//    email.setSmtpPort(GOOGLE_SMTP_PORT);
//    email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
//    email.setSSLOnConnect(true);
//    email.setFrom(FROM);
//    email.setSubject(sub);
//    email.setMsg(msg);
//    email.addTo(to);
//    email.send();
//  }
}
