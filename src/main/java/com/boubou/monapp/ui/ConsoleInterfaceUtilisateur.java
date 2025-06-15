package com.boubou.monapp.ui;

import com.boubou.monapp.model.Employe;
import com.boubou.monapp.model.Notification;
import com.boubou.monapp.persistence.GestionnairePersistance;
import com.boubou.monapp.service.ServiceNotification;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Gère l'interface utilisateur en console pour le système de notification.
 */
public class ConsoleInterfaceUtilisateur {
    private final ServiceNotification service;
    private final List<Employe> tousLesEmployes;
    private final GestionnairePersistance persistance;
    private final Scanner scanner;

    public ConsoleInterfaceUtilisateur(ServiceNotification service, List<Employe> employes) {
        this.service = service;
        this.tousLesEmployes = employes;
        this.persistance = new GestionnairePersistance();
        this.scanner = new Scanner(System.in);
    }

    public void lancer() {
        int choix;
        do {
            afficherMenu();
            try {
                choix = Integer.parseInt(scanner.nextLine());
                traiterChoix(choix);
            } catch (NumberFormatException e) {
                System.out.println("ERREUR : Veuillez entrer un numéro valide.");
                choix = -1;
            }
        } while (choix != 0);
        System.out.println("Au revoir !");
        sauvegarderDonnees(); // Sauvegarde finale
        scanner.close();
    }

    private void afficherMenu() {
        System.out.println("\n===== MENU PRINCIPAL =====");
        System.out.println("1. Afficher la liste des employés");
        System.out.println("2. Gérer l'abonnement d'un employé à un canal");
        System.out.println("3. Envoyer une notification");
        System.out.println("4. Afficher les notifications d'un employé");
        System.out.println("5. Vérifier les abonnements d'un employé");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private void traiterChoix(int choix) {
        switch (choix) {
            case 1 -> gererAffichageEmployes();
            case 2 -> gererAbonnementCanal();
            case 3 -> gererEnvoiMessage();
            case 4 -> gererAffichageNotifications();
            case 5 -> gererVerificationAbonnements();
            case 0 -> {}
            default -> System.out.println("ERREUR : Choix invalide.");
        }
        if (choix != 0) attendreEntree();
    }

    private void gererAffichageEmployes() {
        System.out.println("\n--- Liste des employés ---");
        for (int i = 0; i < tousLesEmployes.size(); i++) {
            Employe e = tousLesEmployes.get(i);
            System.out.printf("%d. %s (%s)\n", i + 1, e.getNom(), e.getEmail());
        }
    }

    private void gererAbonnementCanal() {
        System.out.println("\n--- Gérer un abonnement ---");
        Employe employe = selectionnerEmploye();
        if (employe == null) return;

        service.ajouterObserveur(employe);

        System.out.println("Canaux disponibles : " + service.getNomCanauxDisponibles());
        System.out.print("Entrez le nom du canal : ");
        String nomCanal = scanner.nextLine().toUpperCase();

        if (!service.getNomCanauxDisponibles().contains(nomCanal)) {
            System.out.println("ERREUR: Ce canal n'existe pas.");
            return;
        }

        System.out.print("Action : (1) Abonner, (2) Désabonner ? ");
        try {
            int choixAction = Integer.parseInt(scanner.nextLine());
            if (choixAction == 1) {
                employe.abonnerAuCanal(nomCanal);
                System.out.println("Succès : " + employe.getNom() + " est maintenant abonné à " + nomCanal);
                sauvegarderDonnees();
            } else if (choixAction == 2) {
                employe.desabonnerDuCanal(nomCanal);
                System.out.println("Succès : " + employe.getNom() + " n'est plus abonné à " + nomCanal);
                sauvegarderDonnees();
            } else {
                System.out.println("ERREUR: Action invalide.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERREUR : Entrée invalide.");
        }
    }

    private void gererEnvoiMessage() {
        System.out.println("\n--- Envoyer une notification ---");
        System.out.println("Qui envoie le message ?");
        Employe expediteur = selectionnerEmploye();
        if (expediteur == null) return;

        // Supprimé : service.ajouterObserveur(expediteur); // L'expéditeur n'a pas besoin d'être abonné

        System.out.print("Entrez votre message : ");
        String message = scanner.nextLine();

        service.notifierObserveurs(message, expediteur);
        sauvegarderDonnees();
    }

    private void gererAffichageNotifications() {
        System.out.println("\n--- Afficher l'historique ---");
        Employe employe = selectionnerEmploye();
        if (employe == null) return;

        List<Notification> notifications = employe.getNotificationsRecues();
        if (notifications.isEmpty()) {
            System.out.println(employe.getNom() + " n'a aucune notification.");
        } else {
            System.out.println("Notifications pour " + employe.getNom() + " :");
            notifications.forEach(System.out::println);
        }
    }

    private void gererVerificationAbonnements() {
        System.out.println("\n--- Vérifier les abonnements ---");
        Employe employe = selectionnerEmploye();
        if (employe == null) return;

        boolean estAbonneService = employe.estAbonneService();
        System.out.println("Abonné au service : " + (estAbonneService ? "Oui" : "Non"));

        Set<String> canaux = employe.getCanauxAbonnes();
        System.out.println("Abonné aux canaux : " + (canaux.isEmpty() ? "Aucun" : canaux));
    }

    private Employe selectionnerEmploye() {
        gererAffichageEmployes();
        System.out.print("Choisissez un numéro : ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < tousLesEmployes.size()) {
                return tousLesEmployes.get(index);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("ERREUR : Sélection invalide.");
            return null;
        }
        System.out.println("ERREUR : Sélection invalide.");
        return null;
    }

    private void attendreEntree() {
        System.out.print("\nAppuyez sur Entrée pour continuer...");
        scanner.nextLine();
    }

    private void sauvegarderDonnees() {
        try {
            persistance.sauvegarder(tousLesEmployes);
            System.out.println("Données sauvegardées avec succès.");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
        }
    }
}