/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un bien immobilier.
 */
public class Bien implements Serializable {
    private static final long serialVersionUID = 1L; // Version de sérialisation

    private String idBien; 
    private String adresse;
    private double prix;
    private String typeBien;
    private int nbrPieces;
    private int nbrEtages;
    private double surface;
    private String etatBien; // Nouvel attribut pour l'état du bien (ex : "À vendre", "Loué")

    private final String fichierBiens; // Chemin du fichier pour la sérialisation
    private List<Bien> listeBiens; // Liste des biens

    // Constructeur par défaut
    public Bien(String fichierBiens) {
        this.fichierBiens = fichierBiens;
        this.listeBiens = chargerBiens(); // Charger les biens depuis le fichier
    }

    // Constructeur avec paramètres
    public Bien(String idBien, String adresse, double prix, String typeBien,
                int nbrPieces, int nbrEtages, double surface, String etatBien) {
        this.idBien = idBien;
        this.adresse = adresse;
        this.prix = prix;
        this.typeBien = typeBien;
        this.nbrPieces = nbrPieces;
        this.nbrEtages = nbrEtages;
        this.surface = surface;
        this.etatBien = etatBien;
        this.fichierBiens = null; // Pas utilisé pour les instances individuelles
    }

    // Getters et Setters
    public String getIdBien() {
        return idBien;
    }

    public void setIdBien(String idBien) {
        this.idBien = idBien;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix doit être positif.");
        }
        this.prix = prix;
    }

    public String getTypeBien() {
        return typeBien;
    }

    public void setTypeBien(String typeBien) {
        this.typeBien = typeBien;
    }

    public int getNbrPieces() {
        return nbrPieces;
    }

    public void setNbrPieces(int nbrPieces) {
        this.nbrPieces = nbrPieces;
    }

    public int getNbrEtages() {
        return nbrEtages;
    }

    public void setNbrEtages(int nbrEtages) {
        this.nbrEtages = nbrEtages;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public String getEtatBien() {
        return etatBien;
    }

    public void setEtatBien(String etatBien) {
        this.etatBien = etatBien;
    }

    // Méthode pour afficher les détails d'un bien
    public void afficherDetails() {
        System.out.println("ID : " + idBien);
        System.out.println("Adresse : " + adresse);
        System.out.println("Prix : " + prix);
        System.out.println("Surface : " + surface + " m²");
        System.out.println("Type : " + typeBien);
        System.out.println("Nombre de pièces : " + nbrPieces);
        System.out.println("Nombre d'étages : " + nbrEtages);
        System.out.println("État : " + etatBien);
    }

    // Méthode pour ajouter un bien
    public void ajouterBien(Bien bien) {
        bien.setIdBien(genererIdCodifie().toUpperCase().trim()); // Générer un ID unique
        listeBiens.add(bien); // Ajouter le bien à la liste
        sauvegarderBiens(); // Sauvegarder les biens dans le fichier
        System.out.println("Bien ajouté : " + bien.getIdBien());
    }

    // Méthode pour supprimer un bien
    public boolean supprimerBien(String idBien) {
        boolean removed = listeBiens.removeIf(b -> b.getIdBien().equals(idBien));
        if (removed) {
            sauvegarderBiens();
        }
        return removed;
    }

    // Méthode pour rechercher un bien
    public Bien rechercherBien(String idBien) {
        String normalizedIdBien = idBien.trim().toUpperCase();

        if (listeBiens == null || listeBiens.isEmpty()) {
            System.out.println("La liste des biens est vide.");
            return null;
        }

        System.out.println("Recherche de l'ID : " + normalizedIdBien);
        listeBiens.forEach(b -> System.out.println(" - " + b.getIdBien()));

        return listeBiens.stream()
                .filter(bien -> bien.getIdBien() != null && bien.getIdBien().trim().toUpperCase().equals(normalizedIdBien))
                .findFirst()
                .orElse(null);
    }

    // Méthode pour générer un ID unique
    private String genererIdCodifie() {
        int idNumerique = listeBiens.isEmpty() ? 1 : listeBiens.stream()
                .mapToInt(b -> {
                    try {
                        return Integer.parseInt(b.getIdBien().split("-")[1]);
                    } catch (Exception e) {
                        return 0; // Retourne 0 en cas d'erreur
                    }
                })
                .max()
                .orElse(0) + 1;

        return String.format("BIEN-%03d", idNumerique);
    }

    // Méthodes pour sauvegarder et charger les biens
    private void sauvegarderBiens() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierBiens))) {
            oos.writeObject(listeBiens);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des biens : " + e.getMessage());
        }
    }

    private List<Bien> chargerBiens() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierBiens))) {
            return (List<Bien>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des biens : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Bien> getTousLesBiens() {
        return Collections.unmodifiableList(listeBiens);
    }
}
