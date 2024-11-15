package com.example.User_Service.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service // Ajoutez cette annotation
public class EmailService {

    private static JavaMailSender emailSender = null;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public static boolean sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("ahmedbouzouita549@gmail.com");  // Utilisez votre propre email

            emailSender.send(message);
            return true;  // Si l'email est envoyé sans problème
        } catch (MailException e) {
            e.printStackTrace();  // Affichez l'erreur complète dans les logs
            return false;  // Si l'email n'a pas pu être envoyé
        }
    }
}
