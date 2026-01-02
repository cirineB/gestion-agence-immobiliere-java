package Controller;

import Model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import View.InterfaceRecherche;
/**
 * Classe contrôleur pour gérer les interactions entre la vue et le modèle.
 */
public class ControleurClient implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField telField;
    private JTextField emailField;
    private JComboBox<String> etatCivilCombo;
    private JComboBox<String> typeDemandeCombo;
    private JComboBox<String> situationProCombo;
    private JTable tableClients;
    private JTextField champRecherche; // Champ dédié pour la recherche
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton rechercherButton;
    private JButton reinitialiserButton;
    private JButton homeButton; // Bouton "Accueil"

    private Client modele; // Attribut pour le modèle

    private final String fichierClients = System.getProperty("user.home") + "/clients.dat"; // Path to the file
    
    public ControleurClient(JTextField nomField, JTextField prenomField, JTextField telField, JTextField emailField,
                            JComboBox<String> etatCivilCombo, JComboBox<String> typeDemandeCombo,
                            JComboBox<String> situationProCombo, JTable tableClients, JTextField champRecherche,
                            JButton ajouterButton, JButton supprimerButton, JButton rechercherButton, JButton reinitialiserButton, JButton homeButton,
                            Client modele) {
        this.nomField = nomField;
        this.prenomField = prenomField;
        this.telField = telField;
        this.emailField = emailField;
        this.etatCivilCombo = etatCivilCombo;
        this.typeDemandeCombo = typeDemandeCombo;
        this.situationProCombo = situationProCombo;
        this.tableClients = tableClients;
        this.champRecherche = champRecherche;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.rechercherButton = rechercherButton;
        this.reinitialiserButton = reinitialiserButton;
        this.homeButton = homeButton; // Stocker le bouton "Accueil"
        this.modele = modele; // Stocker l'instance de Client
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == ajouterButton) {
            ajouterClient();
        } else if (source == supprimerButton) {
            supprimerClient();
        } else if (source == rechercherButton) {
            rechercherClient();
        } else if (source == reinitialiserButton) {
            // Gérer l'action du bouton "Réinitialiser"
            reinitialiserTableau();
        } else if (source == homeButton) {
            // Gérer l'action du bouton "Accueil"
            InterfaceRecherche interfaceRecherche = new InterfaceRecherche();
            interfaceRecherche.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(homeButton)).dispose(); // Fermer la fenêtre actuelle
        }
    }

    // Méthode pour ajouter un client
    private void ajouterClient() {
        try {
            // Récupérer les données des champs
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String tel = telField.getText().trim();
            String email = emailField.getText().trim();
            String etatCivil = (String) etatCivilCombo.getSelectedItem();
            String typeDemande = (String) typeDemandeCombo.getSelectedItem();
            String situationPro = (String) situationProCombo.getSelectedItem();

            // Vérifier que tous les champs sont remplis
            if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty() ||
                etatCivil == null || typeDemande == null || situationPro == null) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
                return;
            }

            // Créer un nouvel objet Client
            Client client = new Client(0, nom, prenom, tel, email, etatCivil, typeDemande, situationPro);

            // Ajouter le client au modèle
            modele.ajouterClient(client);

            // Rafraîchir le tableau
            refreshTable();
            JOptionPane.showMessageDialog(null, "Client ajouté avec succès !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du client : " + ex.getMessage());
        }
    }

    // Méthode pour supprimer un client
    private void supprimerClient() {
        int selectedRow = tableClients.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un client à supprimer !");
            return;
        }

        // Récupérer l'ID du client sélectionné
        int idClient = (int) tableClients.getValueAt(selectedRow, 0);

        // Supprimer le client du modèle
        boolean success = modele.supprimerClient(idClient);
        if (success) {
            refreshTable();
            JOptionPane.showMessageDialog(null, "Client supprimé avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Erreur : Client non trouvé !");
        }
    }

    // Méthode pour rechercher un client
    private void rechercherClient() {
        String recherche = champRecherche.getText().trim(); // Supprimer les espaces
        if (recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID pour rechercher !");
            return;
        }

        try {
            // Convertir l'ID entré en entier
            int idRecherche = Integer.parseInt(recherche);

            DefaultTableModel tableModel = (DefaultTableModel) tableClients.getModel();
            tableModel.setRowCount(0); // Effacer les anciennes données

            // Rechercher le client correspondant à l'ID
            Client client = modele.rechercherClient(idRecherche); // Utiliser le modèle pour rechercher le client
            if (client != null) {
                tableModel.addRow(new Object[]{
                    client.getIdPersonne(),
                    client.getNom(),
                    client.getPrenom(),
                    client.getNumeroTel(),
                    client.getEmail(),
                    client.getEtatCivil(),
                    client.getTypeDemande(),
                    client.getSituationPro()
                });
            } else {
                JOptionPane.showMessageDialog(null, "Aucun client trouvé avec l'ID : " + recherche);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID valide !");
        }
    }

    // Méthode pour réinitialiser le tableau
    private void reinitialiserTableau() {
        champRecherche.setText(""); // Effacer le champ de recherche
        refreshTable(); // Recharger tous les clients
    }

    // Méthode pour rafraîchir le tableau
    public void refreshTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tableClients.getModel();
        tableModel.setRowCount(0); // Effacer les anciennes données

        // Récupérer tous les clients depuis le modèle
        List<Client> clients = modele.getTousLesClients(); // Utiliser le modèle pour obtenir les clients
        for (Client client : clients) {
            tableModel.addRow(new Object[]{
                client.getIdPersonne(),
                client.getNom(),
                client.getPrenom(),
                client.getNumeroTel(),
                client.getEmail(),
                client.getEtatCivil(),
                client.getTypeDemande(),
                client.getSituationPro()
            });
        }
    }

    private List<Client> chargerClients() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierClients))) {
        List<Client> clients = (List<Client>) ois.readObject();
        System.out.println("Clients chargés depuis le fichier : " + clients.size());
        clients.forEach(c -> System.out.println("Chargé : " + c.getIdPersonne()));
        return clients;
    } catch (FileNotFoundException e) {
        System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
        return new ArrayList<>();
    } catch (IOException | ClassNotFoundException e) {
        System.err.println("Erreur lors du chargement des clients : " + e.getMessage());
        return new ArrayList<>();
    }
}
}