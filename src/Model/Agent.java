package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Agent extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String FICHIER_AGENTS = System.getProperty("user.home") + "/agents.dat"; // Fichier pour la persistance des agents
    private String fichierAgents; // Chemin du fichier des clients
    private static List<Agent> listeAgents = new ArrayList<>();
    private static int dernierId = 0;

    // Constructeur par défaut
    public Agent(String fichierAgents) {
        this.fichierAgents = fichierAgents; // Correction du constructeur pour utiliser Agent
        listeAgents = chargerAgents(); // Charger les agents depuis le fichier
    }

    private String adresse; // Champ spécifique à Agent

    public Agent(int idPersonne, String nom, String prenom, String numeroTel, String email, String adresse) {
        super(idPersonne, nom, prenom, numeroTel, email); // Appel au constructeur de Personne
        this.adresse = adresse;
    }

    // Getter et Setter pour adresse
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Méthode pour générer un ID codifié pour un agent.
     * @param agent L'agent pour lequel générer l'ID codifié.
     * @return Un ID unique sous la forme "Agent-001", "Agent-002", etc.
     */
    public static String genererIdCodifie(Agent agent) {
        return String.format("Agent-%03d", agent.getIdPersonne()); // Utilise directement l'ID de la classe Personne
    }

    /**
     * Méthode pour ajouter un agent à la liste.
     * @param agent L'agent à ajouter.
     */
    public static void ajouterAgent(Agent agent) {
        if (!listeAgents.contains(agent)) {
            dernierId++;
            agent.setIdPersonne(dernierId); // Assigner un identifiant unique
            listeAgents.add(agent);
            sauvegarderAgents(); // Sauvegarde après ajout
            System.out.println("Agent ajouté et sauvegardé : " + agent);
        } else {
            System.out.println("Agent déjà existant : " + agent);
        }
        System.out.println("Liste des agents actuelle : " + listeAgents);
    }

    /**
     * Méthode pour supprimer un agent de la liste.
     * @param agent L'agent à supprimer.
     */
    public static void supprimerAgent(Agent agent) {
        if (listeAgents.remove(agent)) {
            sauvegarderAgents(); // Sauvegarde après suppression
            System.out.println("Agent supprimé et sauvegardé : " + agent);
        } else {
            System.out.println("Agent non trouvé pour suppression : " + agent);
        }
        System.out.println("Liste des agents actuelle : " + listeAgents);
    }

    /**
     * Méthode pour sauvegarder les agents dans un fichier.
     */
    public static void sauvegarderAgents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_AGENTS))) {
            oos.writeObject(listeAgents);
            System.out.println("Agents sauvegardés avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des agents : " + e.getMessage());
        }
    }

    /**
     * Méthode pour charger les agents depuis un fichier.
     * @return La liste des agents chargés.
     */
    public static List<Agent> chargerAgents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_AGENTS))) {
            List<Agent> agents = (List<Agent>) ois.readObject();
            dernierId = agents.stream()
                .mapToInt(Agent::getIdPersonne)
                .max()
                .orElse(0); // Met à jour le dernier ID
            System.out.println("Agents chargés avec succès !");
            return agents;
        } catch (FileNotFoundException e) {
            System.out.println("Fichier agents.dat non trouvé. Nouvelle liste créée.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des agents : " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Méthode pour valider les agents.
     * Supprime les agents avec des champs null ou invalides.
     */
    public static void validerAgents() {
        if (listeAgents == null) {
            listeAgents = new ArrayList<>(); // Si la liste est null, crée une nouvelle liste vide
        } else {
            listeAgents.removeIf(agent ->
                agent == null ||
                agent.getNom() == null || agent.getPrenom() == null ||
                agent.getNumeroTel() == null || agent.getEmail() == null ||
                agent.getAdresse() == null
            ); // Supprime les agents avec des champs null
        }
        System.out.println("Validation des agents terminée. Liste actuelle : " + listeAgents);
    }

    /**
     * Méthode pour vérifier si un agent existe.
     * @param nomComplet Le nom complet de l'agent (Prénom Nom).
     * @param id L'identifiant de l'agent.
     * @return true si l'agent existe, false sinon.
     */
    public static boolean verifierAgent(String nomComplet, int id) {
        for (Agent agent : listeAgents) {
            String nomCompletAgent = agent.getPrenom() + " " + agent.getNom(); // Concatène Prénom et Nom
            System.out.println("Comparaison avec : " + nomCompletAgent + " - ID: " + agent.getIdPersonne());
            if (nomCompletAgent.equalsIgnoreCase(nomComplet) && agent.getIdPersonne() == id) {
                return true; // L'agent existe
            }
        }
        return false; // L'agent n'existe pas
    }

    /**
     * Méthode pour rechercher un client par son identifiant.
     * @param idPersonne L'identifiant de la personne.
     * @return Le client correspondant ou null s'il n'existe pas.
     */
    public static Agent rechercherAgent(int idPersonne) {
        return listeAgents.stream()
                .filter(agent -> agent.getIdPersonne() == idPersonne)
                .findFirst()
                .orElse(null);

    }

    @Override
    public String toString() {
        return String.format("ID: %d, Nom: %s, Prénom: %s, Téléphone: %s, Email: %s, Adresse: %s",
                getIdPersonne(), getNom(), getPrenom(), getNumeroTel(), getEmail(), adresse);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return idPersonne == agent.idPersonne &&
               Objects.equals(getNom(), agent.getNom()) &&
               Objects.equals(getPrenom(), agent.getPrenom()) &&
               Objects.equals(getNumeroTel(), agent.getNumeroTel()) &&
               Objects.equals(getEmail(), agent.getEmail()) &&
               Objects.equals(getAdresse(), agent.getAdresse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersonne, getNom(), getPrenom(), getNumeroTel(), getEmail(), getAdresse());
    }
    public List<Agent> getTousLesAgents() {
        return new ArrayList<>(listeAgents); // Retourne une copie pour éviter les modifications directes
    }
}