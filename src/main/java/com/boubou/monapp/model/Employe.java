package com.boubou.monapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Représente un employé de l'entreprise qui peut recevoir des notifications.
 * Implémente le pattern Observateur pour être notifié des messages des canaux auxquels il est abonné.
 */
public class Employe implements Observeur {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("email")
    private String email;

    @JsonProperty("notificationsRecues")
    private final List<Notification> notificationsRecues;

    @JsonProperty("canauxAbonnes")
    private final Set<String> canauxAbonnes;

    @JsonProperty("estAbonneService")
    private boolean estAbonneService;

    // Constructeur par défaut pour la désérialisation JSON
    public Employe() {
        this("", "", "");
    }

    /**
     * Constructeur principal pour créer un employé
     * @param id Identifiant unique de l'employé
     * @param nom Nom complet de l'employé
     * @param email Adresse email de l'employé
     */
    public Employe(String id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.notificationsRecues = new ArrayList<>();
        this.canauxAbonnes = new HashSet<>();
        this.estAbonneService = false;
    }

    /**
     * Méthode appelée après la désérialisation JSON pour maintenir la cohérence
     */
    @JsonDeserialize
    private void apresDeserialisation() {
        if (!canauxAbonnes.isEmpty()) {
            estAbonneService = true;
        }
    }

    /**
     * Implémentation de la méthode update de l'interface Observeur
     * @param contenuMessage Le contenu du message reçu
     * @param expediteur L'employé ayant envoyé le message
     * @param nomCanal Le canal par lequel le message a été envoyé
     */
    @Override
    public void update(String contenuMessage, Employe expediteur, String nomCanal) {
        if (estAbonneAuCanal(nomCanal)) {
            Notification notification = creerNotification(contenuMessage, expediteur, nomCanal);
            ajouterNotification(notification);
            afficherRecapitulatifNotification(expediteur, nomCanal, contenuMessage);
        }
    }

    private boolean estAbonneAuCanal(String nomCanal) {
        return canauxAbonnes.contains(nomCanal.toUpperCase());
    }

    private Notification creerNotification(String message, Employe expediteur, String canal) {
        return new Notification(
                message,
                expediteur.getNom(),
                canal,
                LocalDateTime.now()
        );
    }

    private void ajouterNotification(Notification notification) {
        notificationsRecues.add(notification);
    }

    private void afficherRecapitulatifNotification(Employe expediteur, String canal, String message) {
        System.out.printf("Notification reçue pour %s via %s de %s : %s%n",
                this.nom, canal, expediteur.getNom(), message);
    }

    /**
     * Abonne l'employé à un canal spécifique
     * @param nomCanal Le nom du canal à rejoindre
     */
    public void abonnerAuCanal(String nomCanal) {
        canauxAbonnes.add(nomCanal.toUpperCase());
        estAbonneService = true;
    }

    /**
     * Désabonne l'employé d'un canal spécifique
     * @param nomCanal Le nom du canal à quitter
     */
    public void desabonnerDuCanal(String nomCanal) {
        canauxAbonnes.remove(nomCanal.toUpperCase());
    }

    /**
     * Active l'abonnement au service de notification
     */
    public void abonnerService() {
        estAbonneService = true;
    }

    /**
     * Désactive l'abonnement au service de notification et vide les abonnements
     */
    public void desabonnerService() {
        estAbonneService = false;
        canauxAbonnes.clear();
    }

    // --- Méthodes d'accès ---

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public List<Notification> getNotificationsRecues() {
        return Collections.unmodifiableList(notificationsRecues);
    }

    public Set<String> getCanauxAbonnes() {
        return Collections.unmodifiableSet(canauxAbonnes);
    }

    public boolean estAbonneService() {
        return estAbonneService;
    }

    // --- Méthodes utilitaires ---

    public void recevoirNotification(Notification notification) {
        notificationsRecues.add(notification);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employe employe = (Employe) o;
        return Objects.equals(id, employe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employe{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", nbNotifications=" + notificationsRecues.size() +
                ", canauxAbonnes=" + canauxAbonnes +
                '}';
    }
}