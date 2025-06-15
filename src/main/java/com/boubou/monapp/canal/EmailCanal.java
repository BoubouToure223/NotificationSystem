package com.boubou.monapp.canal;

import com.boubou.monapp.model.Employe;
import com.boubou.monapp.model.Notification;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Implémentation du canal de notification par Email.
 * Utilise Jakarta Mail pour envoyer de vrais emails.
 */
public class EmailCanal implements Canal {

    // --- À CONFIGURER PAR VOS SOINS ---
    private final String username = System.getenv("EMAIL_USERNAME");
    private final String password = System.getenv("EMAIL_PASSWORD");


    // ------------------------------------

    @Override
    public String getNom() {
        return "EMAIL";
    }

    @Override
    public void envoyer(Employe destinataire, String contenuMessage, Employe expediteur) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "false"); // Disable TLS
        prop.put("mail.smtp.ssl.enable", "true");      // Enable SSL
        prop.put("mail.smtp.host", System.getenv("SMTP_HOST"));
        prop.put("mail.smtp.port", System.getenv("SMTP_PORT"));

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire.getEmail()));
            message.setSubject("Nouvelle notification de la part de " + expediteur.getNom());
            message.setText("Bonjour " + destinataire.getNom() + ",\n\nVous avez reçu le message suivant :\n\n\""
                    + contenuMessage + "\"\n\nCordialement,\nLe système de notification.");

            System.out.println("LOG: Envoi d'un email réel à " + destinataire.getEmail() + "...");
            Transport.send(message);
            System.out.println("LOG: Email envoyé avec succès !");

            destinataire.recevoirNotification(new Notification(contenuMessage,
                    expediteur.getNom(), this.getNom(), java.time.LocalDateTime.now()));

        } catch (MessagingException e) {
            System.err.println("ERREUR CRITIQUE: L'envoi de l'email a échoué !");
            e.printStackTrace();
        }
    }
}