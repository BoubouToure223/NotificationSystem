package com.boubou.monapp.canal;

import com.boubou.monapp.model.Employe;

/**
 * Contrat (Interface) pour tous les canaux de notification.
 * Assure que toute nouvelle méthode de notification (SMS, Slack...)
 * respectera une structure commune.
 */
public interface Canal {
    String getNom();
    void envoyer(Employe destinataire, String contenuMessage, Employe expediteur);
}