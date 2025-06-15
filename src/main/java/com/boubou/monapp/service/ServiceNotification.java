package com.boubou.monapp.service;

import com.boubou.monapp.model.Employe;
import com.boubou.monapp.model.Observeur;

import java.util.Set;

/**
 * Contrat pour le service de notification, implémentant le pattern Observer.
 */
public interface ServiceNotification {
    void ajouterObserveur(Observeur employe);
    void supprimerObserveur(Observeur employe);
    boolean estAbonne(Observeur employe);
    void notifierObserveurs(String contenuMessage, Employe expediteur);
    Set<String> getNomCanauxDisponibles();

    // Méthodes pour compatibilité avec l'ancienne interface
    void abonner(Employe employe);
    void desabonner(Employe employe);
    boolean estAbonne(Employe employe);
    void notifierAbonnes(String contenuMessage, Employe expediteur);
}