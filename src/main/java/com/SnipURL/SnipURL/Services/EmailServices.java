package com.SnipURL.SnipURL.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServices  {
    @Autowired
    private JavaMailSender javaMailSender;
    public void messages(String to, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("failed to send msg" + e.getMessage());
        }
        }
    }

