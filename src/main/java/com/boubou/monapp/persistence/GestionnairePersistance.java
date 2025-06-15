package com.boubou.monapp.persistence;

import com.boubou.monapp.model.Employe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Manages JSON persistence for employee data, including subscriptions and notifications.
 * Follows SRP by handling only serialization/deserialization tasks.
 */
public class GestionnairePersistance {
    private static final String FICHIER_JSON = "donnees_employes.json"; // JSON file for employee data
    private final ObjectMapper mapper;

    /**
     * Constructor initializes ObjectMapper with indented output and JavaTimeModule for LocalDateTime.
     */
    public GestionnairePersistance() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT); // Pretty-print JSON
        this.mapper.registerModule(new JavaTimeModule()); // Support for LocalDateTime
    }

    /**
     * Saves the list of employees, including their subscriptions and notifications, to JSON.
     * @param employes List of employees to save.
     * @throws IOException If file writing fails.
     */
    public void sauvegarder(List<Employe> employes) throws IOException {
        try {
            File fichier = new File(FICHIER_JSON);
            System.out.println("Sauvegarde dans : " + fichier.getAbsolutePath());
            mapper.writeValue(fichier, employes);
            System.out.println("Données des employés, abonnements et notifications sauvegardées dans " + FICHIER_JSON);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
            throw e;
        }
    }

    /**
     * Loads the list of employees, including their subscriptions and notifications, from JSON.
     * @return List of employees or null if file doesn't exist or loading fails.
     */
    public List<Employe> charger() {
        try {
            File fichier = new File(FICHIER_JSON);
            System.out.println("Chargement depuis : " + fichier.getAbsolutePath());
            if (fichier.exists()) {
                List<Employe> employes = mapper.readValue(fichier,
                        mapper.getTypeFactory().constructCollectionType(List.class, Employe.class));
                System.out.println("Chargement réussi : " + employes.size() + " employés chargés.");
                return employes;
            } else {
                System.out.println("Aucun fichier JSON trouvé à : " + fichier.getAbsolutePath());
                return null;
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}