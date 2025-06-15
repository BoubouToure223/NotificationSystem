package com.boubou.monapp.model;

/**
 * Interface pour les entités capables de recevoir des notifications.
 */
public interface Observeur {
    void update(String contenuMessage, Employe expediteur, String canal);
}