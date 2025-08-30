package com.kartik.Payment_Gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
  private JavaMailSender javaMailSender;

    public void sendMail(String toEmail,String name ,String course ,double amount){
        try {
            SimpleMailMessage message  = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("✅ Payment Successful"+course);
            message.setText("Hi " + name + ",\n\nThank you for enrolling in the course: " + course +
                    ".\nWe have received your payment of ₹" + amount + ".");

            javaMailSender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}
