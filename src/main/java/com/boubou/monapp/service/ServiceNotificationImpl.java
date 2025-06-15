package com.boubou.monapp.service;

import com.boubou.monapp.canal.Canal;
import com.boubou.monapp.canal.CanalProvider;
import com.boubou.monapp.model.Employe;
import com.boubou.monapp.model.Observeur;

import java.util.*;

/**
 * Implémentation du service de notification utilisant le pattern Observer.
 * Gère les abonnés et orchestre l'envoi des notifications via les canaux.
 */
public class ServiceNotificationImpl implements ServiceNotification {
    private final Map<String, Canal> canauxDisponibles;
    private final List<Observeur> observateurs;
    private final List<Employe> tousLesEmployes;

    public ServiceNotificationImpl(List<Employe> employes, CanalProvider canalProvider) {
        if (employes == null) {
            throw new IllegalArgumentException("La liste des employés ne peut pas être null.");
        }
        this.canauxDisponibles = new HashMap<>();
        this.observateurs = new ArrayList<>();
        this.tousLesEmployes = employes;
        initialiserCanaux(canalProvider);
    }

    private void initialiserCanaux(CanalProvider canalProvider) {
        if (canalProvider == null || canalProvider.getCanaux() == null) {
            System.err.println("ERREUR: CanalProvider non valide ou aucun canal fourni.");
            return;
        }
        for (Canal canal : canalProvider.getCanaux()) {
            if (canal != null && canal.getNom() != null) {
                if (canauxDisponibles.containsKey(canal.getNom())) {
                    System.err.println("AVERTISSEMENT: Canal avec le nom " + canal.getNom() + " déjà existant, ignoré.");
                    continue;
                }
                this.canauxDisponibles.put(canal.getNom(), canal);
            }
        }
        if (canauxDisponibles.isEmpty()) {
            System.err.println("AVERTISSEMENT: Aucun canal de notification disponible.");
        }
    }

    @Override
    public void ajouterObserveur(Observeur employe) {
        if (employe != null && !observateurs.contains(employe)) {
            observateurs.add(employe);
            if (employe instanceof Employe) {
                ((Employe) employe).abonnerService();
            }
        }
    }

    @Override
    public void supprimerObserveur(Observeur employe) {
        observateurs.remove(employe);
        if (employe instanceof Employe) {
            ((Employe) employe).desabonnerService();
        }
    }

    @Override
    public boolean estAbonne(Observeur employe) {
        return employe != null && observateurs.contains(employe);
    }

    @Override
    public void notifierObserveurs(String contenuMessage, Employe expediteur) {
        if (expediteur == null || contenuMessage == null || contenuMessage.trim().isEmpty()) {
            System.out.println("ERREUR: Expéditeur ou message invalide.");
            return;
        }

        System.out.println("\n--- Début de l'envoi des notifications ---");
        for (Observeur observeur : new ArrayList<>(observateurs)) {
            if (observeur instanceof Employe && !observeur.equals(expediteur)) {
                Employe abonne = (Employe) observeur;
                for (String nomCanal : abonne.getCanauxAbonnes()) {
                    Canal canal = this.canauxDisponibles.get(nomCanal);
                    if (canal != null) {
                        canal.envoyer(abonne, contenuMessage, expediteur);
                    }
                }
            }
        }
        System.out.println("--- Fin de l'envoi des notifications ---\n");
    }

    @Override
    public Set<String> getNomCanauxDisponibles() {
        return new HashSet<>(canauxDisponibles.keySet());
    }

    // Méthodes de compatibilité
    @Override
    public void abonner(Employe employe) {
        ajouterObserveur(employe);
    }

    @Override
    public void desabonner(Employe employe) {
        supprimerObserveur(employe);
    }

    @Override
    public boolean estAbonne(Employe employe) {
        return estAbonne((Observeur) employe);
    }

    @Override
    public void notifierAbonnes(String contenuMessage, Employe expediteur) {
        notifierObserveurs(contenuMessage, expediteur);
    }
}