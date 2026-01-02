package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un client.
 */
public class Client extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    private String etatCivil;
    private String typeDemande;
    private String situationPro;

    private final String fichierClients; // Chemin du fichier pour la persistance
    private List<Client> listeClients; // Liste des clients

    // Constructeur pour initialiser le fichier
    public Client(String fichierClients) {
        this.fichierClients = fichierClients;
        this.listeClients = chargerClients(); // Charger les clients depuis le fichier
    }

    // Constructeur avec paramètres
    public Client(int idPersonne, String nom, String prenom, String numeroTel, String email,
                  String etatCivil, String typeDemande, String situationPro) {
        super(idPersonne, nom, prenom, numeroTel, email);
        this.etatCivil = etatCivil;
        this.typeDemande = typeDemande;
        this.situationPro = situationPro;
        this.fichierClients = null; // Pas utilisé pour les instances individuelles
    }

    // Getters et Setters
    public String getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public String getSituationPro() {
        return situationPro;
    }

    public void setSituationPro(String situationPro) {
        this.situationPro = situationPro;
    }

    // Méthode pour afficher les détails d'un client
    public void afficherDetails() {
        System.out.println("ID : " + getIdPersonne());
        System.out.println("Nom : " + getNom());
        System.out.println("Prénom : " + getPrenom());
        System.out.println("Téléphone : " + getNumeroTel());
        System.out.println("Email : " + getEmail());
        System.out.println("État Civil : " + etatCivil);
        System.out.println("Type de Demande : " + typeDemande);
        System.out.println("Situation Pro. : " + situationPro);
    }

    // Méthode pour ajouter un client
    public void ajouterClient(Client client) {
        client.setIdPersonne(genererIdUnique()); // Générer un ID unique
        listeClients.add(client); // Ajouter le client à la liste
        sauvegarderClients(); // Sauvegarder les clients dans le fichier
        System.out.println("Client ajouté : " + client.getIdPersonne());
    }

    // Méthode pour supprimer un client
    public boolean supprimerClient(int idPersonne) {
        boolean removed = listeClients.removeIf(c -> c.getIdPersonne() == idPersonne);
        if (removed) {
            sauvegarderClients();
        }
        return removed;
    }

    // Méthode pour rechercher un client
    public Client rechercherClient(int idPersonne) {
        return listeClients.stream()
                .filter(client -> client.getIdPersonne() == idPersonne)
                .findFirst()
                .orElse(null);
    }

    // Méthode pour générer un ID unique
    private int genererIdUnique() {
        return listeClients.isEmpty() ? 1 : listeClients.stream()
                .mapToInt(Client::getIdPersonne)
                .max()
                .orElse(0) + 1;
    }

    // Méthodes pour sauvegarder et charger les clients
    private void sauvegarderClients() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierClients))) {
            oos.writeObject(listeClients);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des clients : " + e.getMessage());
        }
    }

    private List<Client> chargerClients() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierClients))) {
            return (List<Client>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des clients : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Client> getTousLesClients() {
        return Collections.unmodifiableList(listeClients);
    }
    
    public Client rechercherClientParId(int idClient) {
        return listeClients.stream()
            .filter(client -> client.getIdPersonne() == idClient)
            .findFirst()
            .orElse(null);
    }
    

}

