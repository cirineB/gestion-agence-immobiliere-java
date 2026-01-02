package Controller;

import Model.Agent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe contrôleur pour gérer les agents.
 */
public class ControleurAgent implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField telField;
    private JTextField emailField;
    private JTextField adresseField;
    private JPanel cardContainer; // Conteneur pour afficher les cartes des agents

    // Attribut pour le modèle
    private Agent modele;

    // Liste des agents
    private List<Agent> listeAgents;

    /**
     * Constructeur par défaut.
     */
    public ControleurAgent() {
        chargerAgents(); // Charger les agents au démarrage
    }

    /**
     * Constructeur avec le modèle et les composants graphiques nécessaires.
     *
     * @param modele        Le modèle (Agent).
     * @param nomField      Champ pour le nom.
     * @param prenomField   Champ pour le prénom.
     * @param telField      Champ pour le téléphone.
     * @param emailField    Champ pour l'email.
     * @param adresseField  Champ pour l'adresse.
     * @param cardContainer Conteneur pour afficher les cartes des agents.
     */
    public ControleurAgent(Agent modele, JTextField nomField, JTextField prenomField, JTextField telField,
                           JTextField emailField, JTextField adresseField, JPanel cardContainer) {
        this.modele = modele;
        this.nomField = nomField;
        this.prenomField = prenomField;
        this.telField = telField;
        this.emailField = emailField;
        this.adresseField = adresseField;
        this.cardContainer = cardContainer;
        this.listeAgents = new ArrayList<>();
        chargerAgents(); // Charger les agents au démarrage
    }

    /**
     * Méthode pour gérer les actions des boutons.
     *
     * @param e L'événement déclenché.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Ajouter":
                ajouterAgentAction();
                break;

            case "Supprimer":
                supprimerAgentAction();
                break;

            default:
                System.out.println("Commande inconnue : " + command);
        }
    }

    /**
     * Action pour ajouter un agent.
     */
    private void ajouterAgentAction() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String tel = telField.getText().trim();
        String email = emailField.getText().trim();
        String adresse = adresseField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty() || adresse.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
            return;
        }

        // Création d'un nouvel agent
        Agent agent = new Agent(0, nom, prenom, tel, email, adresse);
        ajouterAgent(agent);

        // Ajout de la carte dans l'interface
        ajouterCarteAgent(agent);

        // Réinitialisation du formulaire
        resetForm();
        JOptionPane.showMessageDialog(null, "Agent ajouté avec succès !");
    }

    /**
     * Action pour supprimer un agent.
     */
    private void supprimerAgentAction() {
        // Logique pour supprimer un agent (à implémenter si nécessaire)
        JOptionPane.showMessageDialog(null, "Action de suppression non implémentée.");
    }

    /**
     * Méthode pour ajouter un agent.
     *
     * @param agent L'agent à ajouter.
     */
    public void ajouterAgent(Agent agent) {
        Agent.ajouterAgent(agent); // Appel à la méthode statique de la classe Agent
        sauvegarderAgents(); // Sauvegarde après ajout
    }

    /**
     * Méthode pour supprimer un agent.
     *
     * @param agent L'agent à supprimer.
     */
    public void supprimerAgent(Agent agent) {
        Agent.supprimerAgent(agent); // Appel à la méthode statique de la classe Agent
    }

    /**
     * Méthode pour sauvegarder les agents.
     */
    private void sauvegarderAgents() {
        Agent.sauvegarderAgents(); // Appel à la méthode statique de la classe Agent
    }

    /**
     * Méthode pour charger les agents.
     */
    private void chargerAgents() {
        listeAgents = Agent.chargerAgents(); // Appel à la méthode statique de la classe Agent
    }

    /**
     * Méthode pour ajouter une carte d'agent dans l'interface.
     *
     * @param agent L'agent à afficher.
     */
    private void ajouterCarteAgent(Agent agent) {
        JPanel card = new JPanel();
        card.add(new JLabel(agent.getNom() + " " + agent.getPrenom()));
        JButton deleteButton = new JButton("Supprimer");
        card.add(deleteButton);
        ajouterActionSupprimerCarte(deleteButton, card, agent, cardContainer);
        cardContainer.add(card);
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    /**
     * Méthode pour réinitialiser le formulaire.
     */
    private void resetForm() {
        nomField.setText("");
        prenomField.setText("");
        telField.setText("");
        emailField.setText("");
        adresseField.setText("");
    }

    public List<Agent> getTousLesAgents() {
        return new ArrayList<>(listeAgents); // Retourne une copie pour éviter les modifications directes
    }

    public void ajouterActionSupprimerCarte(JButton deleteButton, JPanel card, Agent agent, JPanel cardContainer) {
        deleteButton.addActionListener(e -> {
            // Supprime l'agent du modèle
            supprimerAgent(agent);

            // Supprime la carte du conteneur
            cardContainer.remove(card);

            // Met à jour l'interface
            cardContainer.revalidate();
            cardContainer.repaint();

            JOptionPane.showMessageDialog(null, "Agent supprimé avec succès !");
        });
    }
}