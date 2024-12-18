package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private JavaMailSender mailSender;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public Boolean sendMessage(
    String reciepent,
    String subject,
    String text
  ) {
    String timestamp = String.valueOf(System.currentTimeMillis());

    try {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("noreply@toserba.com");
      message.setTo(reciepent);
      message.setSubject("TOSERBA " + subject + "- " + timestamp);
      message.setText(text);

      mailSender.send(message);

      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
