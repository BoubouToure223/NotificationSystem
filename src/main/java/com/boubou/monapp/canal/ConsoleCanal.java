package com.boubou.monapp.canal;

import com.boubou.monapp.model.Employe;
import com.boubou.monapp.model.Notification;

/**
 * Implémentation du canal de notification pour la console.
 */
public class ConsoleCanal implements Canal {
    @Override
    public String getNom() {
        return "CONSOLE";
    }

    @Override
    public void envoyer(Employe destinataire, String contenuMessage, Employe expediteur) {
        String messageAffiche = String.format("Notification Console pour %s de la part de %s: %s",
                destinataire.getNom(), expediteur.getNom(), contenuMessage);
        System.out.println(messageAffiche);

        destinataire.recevoirNotification(new Notification(contenuMessage,
                expediteur.getNom(), this.getNom(), java.time.LocalDateTime.now()));
    }
}