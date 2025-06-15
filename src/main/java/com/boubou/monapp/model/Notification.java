package com.boubou.monapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Représente une notification reçue et stockée dans l'historique d'un employé.
 */
public class Notification {
    @JsonProperty("message")
    private String message;
    @JsonProperty("nomExpediteur")
    private String nomExpediteur;
    @JsonProperty("canalUtilise")
    private String canalUtilise;
    @JsonProperty("dateEnvoi")
    private LocalDateTime dateEnvoi;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Default constructor for Jackson
    public Notification() {
    }

    public Notification(String message, String nomExpediteur, String canalUtilise, LocalDateTime dateEnvoi) {
        this.message = message;
        this.nomExpediteur = nomExpediteur;
        this.canalUtilise = canalUtilise;
        this.dateEnvoi = dateEnvoi;
    }

    // Getters required for Jackson serialization
    public String getMessage() {
        return message;
    }

    public String getNomExpediteur() {
        return nomExpediteur;
    }

    public String getCanalUtilise() {
        return canalUtilise;
    }

    public LocalDateTime getDateEnvoi() {
        return dateEnvoi;
    }

    @Override
    public String toString() {
        return String.format("[%s - %s] De %s: %s",
                dateEnvoi.format(FORMATTER), canalUtilise, nomExpediteur, message);
    }
}