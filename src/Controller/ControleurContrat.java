package Controller;

import Model.Agent;
import Model.Bien;
import Model.Client;
import Model.Contrat;
import View.GestionContrat;
import View.InterfaceRecherche;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Classe contrôleur pour gérer les interactions entre la vue et le modèle pour les contrats.
 */
public class ControleurContrat implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JTextField dateContratField;
    private JTextField montantField;
    private JComboBox<String> typeContratField;
    private JTextField idClientField;
    private JTextField idBienField;
    private JTextField idAgentField;
    private JTextField champRecherche; // Champ dédié pour la recherche
    private JButton ajouterButton;
    private JButton rechercherButton;
    private JButton reinitialiserButton;
    private JButton homeButton; // Bouton "Accueil"

    private Contrat modele; // Attribut pour le modèle
    private GestionContrat vue; // Référence à la vue

    public ControleurContrat(JTextField dateContratField, JTextField montantField, JComboBox<String> typeContratField,
                             JTextField idClientField, JTextField idBienField, JTextField idAgentField,
                             JTextField champRecherche, JButton ajouterButton, JButton rechercherButton,
                             JButton reinitialiserButton, JButton homeButton, Contrat modele, GestionContrat vue) {
        this.dateContratField = dateContratField;
        this.montantField = montantField;
        this.typeContratField = typeContratField;
        this.idClientField = idClientField;
        this.idBienField = idBienField;
        this.idAgentField = idAgentField;
        this.champRecherche = champRecherche;
        this.ajouterButton = ajouterButton;
        this.rechercherButton = rechercherButton;
        this.reinitialiserButton = reinitialiserButton;
        this.homeButton = homeButton; // Stocker le bouton "Accueil"
        this.modele = modele; // Stocker l'instance de Contrat
        this.vue = vue; // Stocker la référence à la vue
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == ajouterButton) {
            ajouterContrat();
        } else if (source == rechercherButton) {
            rechercherContrat();
        } else if (source == reinitialiserButton) {
            reinitialiserListe();
        } else if (source == homeButton) {
            // Gérer l'action du bouton "Accueil"
            InterfaceRecherche interfaceRecherche = new InterfaceRecherche();
            interfaceRecherche.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(homeButton)).dispose(); // Fermer la fenêtre actuelle
        } else if (source instanceof JButton) {
            JButton button = (JButton) source;

            // Vérifier si le bouton a un ActionCommand (ID du contrat)
            String idContrat = button.getActionCommand();
            if (idContrat != null) {
                supprimerContrat(idContrat); // Appeler la méthode pour supprimer le contrat
            }
        }
    }

    // Méthode pour ajouter un contrat
    private void ajouterContrat() {
        try {
            // Récupérer les données des champs
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateContrat = dateFormat.parse(dateContratField.getText().trim());
            double montant = Double.parseDouble(montantField.getText().trim());
            String typeContrat = (String) typeContratField.getSelectedItem();
            String idClient = idClientField.getText().trim();
            String idBien = idBienField.getText().trim();
            String idAgent = idAgentField.getText().trim();

            // Vérifier que tous les champs sont remplis
            if (typeContrat == null || idClient.isEmpty() || idBien.isEmpty() || idAgent.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
                return;
            }

            // Rechercher les objets associés
            Client client = rechercherClient(idClient);
            Bien bien = rechercherBien(idBien);
            Agent agent = rechercherAgent(idAgent);

            if (client == null || bien == null || agent == null) {
                JOptionPane.showMessageDialog(null, "Erreur : Client, Bien ou Agent introuvable !");
                return;
            }

            // Créer un nouvel objet Contrat
            Contrat contrat = new Contrat(null, dateContrat, montant, typeContrat, client, bien, agent);

            // Ajouter le contrat au modèle
            modele.ajouterContrat(contrat);

            // Rafraîchir les cartes dans la vue
            refreshCartes();
            JOptionPane.showMessageDialog(null, "Contrat ajouté avec succès !");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer des valeurs valides pour les champs numériques !");
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer une date valide au format dd/MM/yyyy !");
        }
    }

    // Méthode pour rechercher un contrat
    private void rechercherContrat() {
        String recherche = champRecherche.getText().trim(); // Supprimer les espaces
        if (recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID pour rechercher !");
            return;
        }

        // Rechercher le contrat correspondant à l'ID
        Contrat contrat = modele.rechercherContrat(recherche); // Utiliser le modèle pour rechercher le contrat
        if (contrat != null) {
            // Notifier la vue pour afficher uniquement le contrat trouvé
            List<Contrat> resultats = List.of(contrat); // Créer une liste contenant uniquement le contrat trouvé
            vue.afficherCartes(resultats); // Appeler une méthode de la vue pour afficher les résultats
        } else {
            JOptionPane.showMessageDialog(null, "Aucun contrat trouvé avec l'ID : " + recherche);
        }
    }

    // Méthode pour réinitialiser la liste
    private void reinitialiserListe() {
        champRecherche.setText(""); // Effacer le champ de recherche
        vue.afficherCartes(); // Recharger tous les contrats
    }

    // Méthode pour rafraîchir les cartes
    public void refreshCartes() {
        // Récupérer tous les contrats depuis le modèle
        List<Contrat> contrats = modele.getTousLesContrats(); // Utiliser le modèle pour obtenir les contrats

        // Notifier la vue pour afficher les cartes mises à jour
        vue.afficherCartes(contrats); // Appeler la méthode de la vue pour afficher les cartes
    }

    // Méthode pour supprimer un contrat
    public void supprimerContrat(String idContrat) {
        // Supprimer le contrat du modèle
        boolean success = modele.supprimerContrat(idContrat);
        if (success) {
            JOptionPane.showMessageDialog(null, "Contrat supprimé avec succès !");
            // Notifier la vue pour rafraîchir les cartes
            vue.afficherCartes();
        } else {
            JOptionPane.showMessageDialog(null, "Erreur : Contrat non trouvé !");
        }
    }

    // Méthodes pour rechercher les objets associés
    private Client rechercherClient(String idClient) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/clients.dat"))) {
            List<Client> clients = (List<Client>) ois.readObject();
            return clients.stream()
                    .filter(client -> String.valueOf(client.getIdPersonne()).equals(idClient))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche du client : " + e.getMessage());
            return null;
        }
    }

    private Bien rechercherBien(String idBien) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/biens.dat"))) {
            List<Bien> biens = (List<Bien>) ois.readObject();
            return biens.stream()
                    .filter(bien -> String.valueOf(bien.getIdBien()).equals(idBien))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche du bien : " + e.getMessage());
            return null;
        }
    }

    private Agent rechercherAgent(String idAgent) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(System.getProperty("user.home") + "/agents.dat"))) {
            List<Agent> agents = (List<Agent>) ois.readObject();
            return agents.stream()
                    .filter(agent -> String.valueOf(agent.getIdPersonne()).equals(idAgent))
                    .findFirst()
                    .orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la recherche de l'agent : " + e.getMessage());
            return null;
        }
    }
}