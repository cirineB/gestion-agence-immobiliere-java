package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant un contrat.
 */
public class Contrat implements Serializable {
    private static final long serialVersionUID = 1L; // Version pour la sérialisation

    private String idContrat;
    private Date dateContrat;
    private double montant;
    private String typeContrat;

    private Client client; // Association avec Client
    private Bien bien; // Association avec Bien
    private Agent agent; // Association avec Agent

    private final String fichierContrats; // Chemin du fichier pour la sérialisation
    private List<Contrat> listeContrats; // Liste des contrats

    // Constructeur par défaut
    public Contrat(String fichierContrats) {
        this.fichierContrats = fichierContrats;
        this.listeContrats = chargerContrats(); // Charger les contrats depuis le fichier
    }

    // Constructeur avec paramètres
    public Contrat(String idContrat, Date dateContrat, double montant, String typeContrat, Client client, Bien bien, Agent agent) {
        this.idContrat = idContrat;
        this.dateContrat = dateContrat;
        this.montant = montant;
        this.typeContrat = typeContrat;
        this.client = client;
        this.bien = bien;
        this.agent = agent;
        this.fichierContrats = null; // Pas utilisé pour les instances individuelles
    }

    // Getters et Setters
    public String getIdContrat() {
        return idContrat;
    }

    public void setIdContrat(String idContrat) {
        this.idContrat = idContrat;
    }

    public Date getDateContrat() {
        return dateContrat;
    }

    public void setDateContrat(Date dateContrat) {
        this.dateContrat = dateContrat;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant doit être positif.");
        }
        this.montant = montant;
    }

    public String getTypeContrat() {
        return typeContrat;
    }

    public void setTypeContrat(String typeContrat) {
        this.typeContrat = typeContrat;
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

    // Méthode pour afficher les détails d'un contrat
    public void afficherDetails() {
        System.out.println("ID Contrat : " + idContrat);
        System.out.println("Date : " + dateContrat);
        System.out.println("Montant : " + montant);
        System.out.println("Type : " + typeContrat);
        System.out.println("Client : " + (client != null ? client.getNom() : "Non spécifié"));
        System.out.println("Bien : " + (bien != null ? bien.getIdBien() : "Non spécifié"));
        System.out.println("Agent : " + (agent != null ? agent.getNom() : "Non spécifié"));
    }

    // Méthode pour ajouter un contrat
    public void ajouterContrat(Contrat contrat) {
        contrat.setIdContrat(genererIdCodifie().toUpperCase().trim()); // Générer un ID unique
        listeContrats.add(contrat); // Ajouter le contrat à la liste
        sauvegarderContrats(); // Sauvegarder les contrats dans le fichier
        System.out.println("Contrat ajouté : " + contrat.getIdContrat());
    }

    // Méthode pour supprimer un contrat
    public boolean supprimerContrat(String idContrat) {
        boolean removed = listeContrats.removeIf(c -> c.getIdContrat().equals(idContrat));
        if (removed) {
            sauvegarderContrats();
        }
        return removed;
    }

    // Méthode pour rechercher un contrat
    public Contrat rechercherContrat(String idContrat) {
        return listeContrats.stream()
                .filter(contrat -> contrat.getIdContrat().equals(idContrat))
                .findFirst()
                .orElse(null);
    }

    // Méthode pour générer un ID unique
    private String genererIdCodifie() {
        int idNumerique = listeContrats.isEmpty() ? 1 : listeContrats.stream()
                .mapToInt(c -> Integer.parseInt(c.getIdContrat().split("-")[1]))
                .max()
                .orElse(0) + 1;

        return String.format("CONTRAT-%03d", idNumerique);
    }

    // Méthodes pour sauvegarder et charger les contrats
    private void sauvegarderContrats() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichierContrats))) {
            oos.writeObject(listeContrats);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des contrats : " + e.getMessage());
        }
    }

    private List<Contrat> chargerContrats() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierContrats))) {
            return (List<Contrat>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
            return new ArrayList<>(); // Retourne une liste vide si le fichier n'existe pas
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des contrats : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Contrat> getTousLesContrats() {
        return Collections.unmodifiableList(listeContrats);
    }
}