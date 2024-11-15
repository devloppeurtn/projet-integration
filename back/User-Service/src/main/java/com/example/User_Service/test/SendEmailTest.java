package com.example.User_Service.test; // Nouveau package pour les tests

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class SendEmailTest {

    public static void main(String[] args) {
        // Paramètres de l'email
        String to = "ahmedbouzouita770@gmail.com";  // Remplacez par l'email du destinataire
        String from = "ahmedbouzouita549@gmail.com";  // Remplacez par votre email Gmail
        String password = "houssine97";  // Utilisez votre mot de passe d'application (si la validation 2FA est activée)

        // Configuration des propriétés de l'email
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");  // Serveur SMTP de Gmail
        properties.setProperty("mail.smtp.port", "587");  // Port pour l'envoi TLS
        properties.setProperty("mail.smtp.auth", "true");  // Authentification requise
        properties.setProperty("mail.smtp.starttls.enable", "true");  // Activer STARTTLS pour une connexion sécurisée

        // Création de la session avec authentification
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);  // Authentification avec votre email et mot de passe d'application
            }
        });

        try {
            // Création du message
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));  // Adresse de l'expéditeur
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  // Adresse du destinataire
            message.setSubject("Test Email JavaMail");  // Sujet de l'email
            message.setText("Ceci est un email de test envoyé depuis JavaMail.");  // Corps du message

            // Envoi du message
            Transport.send(message);  // Envoi de l'email
            System.out.println("Email envoyé avec succès !");
        } catch (MessagingException e) {
            e.printStackTrace();  // En cas d'erreur, afficher l'exception
        }
    }
}
