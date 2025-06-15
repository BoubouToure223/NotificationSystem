package com.boubou.monapp.ui;

import com.boubou.monapp.canal.DefaultCanalProvider;
import com.boubou.monapp.model.Employe;
import com.boubou.monapp.persistence.GestionnairePersistance;
import com.boubou.monapp.service.ServiceNotification;
import com.boubou.monapp.service.ServiceNotificationImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordonne le système de notification et la persistance des données.
 */
public class ApplicationSystemeNotification {
    private final ServiceNotification service;
    private final List<Employe> tousLesEmployes;
    private final GestionnairePersistance persistance;
    private final ConsoleInterfaceUtilisateur interfaceUtilisateur;

    public ApplicationSystemeNotification() {
        this.tousLesEmployes = new ArrayList<>();
        this.service = new ServiceNotificationImpl(tousLesEmployes, new DefaultCanalProvider());
        this.persistance = new GestionnairePersistance();
        this.interfaceUtilisateur = new ConsoleInterfaceUtilisateur(service, tousLesEmployes);
        initialiserDonnees();
    }

    public static void main(String[] args) {
        ApplicationSystemeNotification app = new ApplicationSystemeNotification();
        app.lancer();
    }

    public void lancer() {
        interfaceUtilisateur.lancer();
        // Sauvegarde gérée par ConsoleInterfaceUtilisateur
    }

    private void initialiserDonnees() {
        List<Employe> employesCharges = persistance.charger();
        if (employesCharges != null && !employesCharges.isEmpty()) {
            tousLesEmployes.addAll(employesCharges);
            System.out.println("Données chargées depuis le fichier JSON : " + tousLesEmployes.size() + " employés.");
            for (Employe e : tousLesEmployes) {
                if (e.estAbonneService()) {
                    service.ajouterObserveur(e);
                } else if (!e.getCanauxAbonnes().isEmpty()) {
                    // Synchroniser si des canaux sont présents
                    e.abonnerService();
                    service.ajouterObserveur(e);
                }
            }
        } else {
            System.out.println("Échec du chargement ou fichier JSON vide, initialisation des données par défaut.");
            tousLesEmployes.add(new Employe("E001", "BOUBOU", "9346baba@gmail.com"));
            tousLesEmployes.add(new Employe("E002", "Issa", "9346baba@gmail.com"));
            tousLesEmployes.add(new Employe("E003", "Oumar", "oumar@example.com"));
            sauvegarderDonnees(); // Sauvegarder les données par défaut
        }
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