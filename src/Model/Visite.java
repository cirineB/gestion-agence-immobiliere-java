/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant une visite.
 */
public class Visite implements Serializable {
    private static final long serialVersionUID = 1L; // Version pour la sérialisation

    private String idVisite;
    private Date dateVisite;
    private String heureVisite;
    private String commentaire;

    private Client client; // Association avec Client
    private Bien bien; // Association avec Bien
    private Agent agent; // Association avec Agent (organise)

    private final String fichierVisites; // Chemin du fichier pour la sérialisation
    private List<Visite> listeVisites; // Liste des visites

    // Constructeur par défaut
    public Visite(String fichierVisites) {
        this.fichierVisites = fichierVisites;
        this.listeVisites = chargerVisites(); // Charger les visites depuis le fichier
    }

    // Constructeur avec paramètres
    public Visite(String idVisite, Date dateVisite, String heureVisite, String commentaire, Client client, Bien bien, Agent agent) {
        this.idVisite = idVisite;
        this.dateVisite = dateVisite;
        this.heureVisite = heureVisite;
        this.commentaire = commentaire;
        this.client = client;
        this.bien = bien;
        this.agent = agent;
        this.fichierVisites = null; // Pas utilisé pour les instances individuelles
    }

    // Getters et Setters
    public String getIdVisite() {
        return idVisite;
    }

    public void setIdVisite(String idVisite) {
        this.idVisite = idVisite;
    }

    public Date getDateVisite() {
        return dateVisite;
    }

    public void setDateVisite(Date dateVisite) {
        this.dateVisite = dateVisite;
    }

    public String getHeureVisite() {
        return heureVisite;
    }

    public void setHeureVisite(String heureVisite) {
        this.heureVisite = heureVisite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Bien getBien() {
        return bien;
    }

    public void setBien(Bien bien) {
        this.bien = bien;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    // Méthode pour afficher les détails d'une visite
    public void afficherDetails() {
        System.out.println("ID Visite : " + idVisite);
        System.out.println("Date : " + dateVisite);
        System.out.println("Heure : " + heureVisite);
        System.out.println("Commentaire : " + commentaire);
        System.out.println("Client : " + (client != null ? client.getNom() : "Non spécifié"));
        System.out.println("Bien : " + (bien != null ? bien.getIdBien() : "Non spécifié"));
        System.out.println("Agent : " + (agent != null ? agent.getNom() : "Non spécifié"));
    }

    // Méthode pour ajouter une visite
    public void ajouterVisite(Visite visite) {
        visite.setIdVisite(genererIdCodifie().toUpperCase().trim()); // Générer un ID unique
        listeVisites.add(visite); // Ajouter la visite à la liste
        sauvegarderVisites(); // Sauvegarder les visites dans le fichier
        System.out.println("Visite ajoutée : " + visite.getIdVisite());
    }

    // Méthode pour supprimer une visite
    public boolean supprimerVisite(String idVisite) {
        boolean removed = listeVisites.removeIf(v -> v.getIdVisite().equals(idVisite));
        if (removed) {
            sauvegarderVisites();
        }
        return removed;
    }

    // Méthode pour rechercher une visite
    public Visite rechercherVisite(String idVisite) {
        return listeVisites.stream()
                .filter(visite -> visite.getIdVisite().equals(idVisite))
                .findFirst()
                .orElse(null);
    }

    // Méthode pour générer un ID unique
    private String genererIdCodifie() {
        int idNumerique = listeVisites.isEmpty() ? 1 : listeVisites.stream()
                .mapToInt(v -> Integer.parseInt(v.getIdVisite().split("-")[1]))
                .max()
                .orElse(0) + 1;

        return String.format("VISITE-%03d", idNumerique);
    }

    // Méthodes pour sauvegarder et charger les visites
    private void sauvegarderVisites() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierVisites))) {
            oos.writeObject(listeVisites);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des visites : " + e.getMessage());
        }
    }

    private List<Visite> chargerVisites() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierVisites))) {
            return (List<Visite>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des visites : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Visite> getToutesLesVisites() {
        return Collections.unmodifiableList(listeVisites);
    }
}



