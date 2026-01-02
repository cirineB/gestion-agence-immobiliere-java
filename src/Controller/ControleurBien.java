package Controller;

import Model.Bien;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import View.InterfaceRecherche;
/**
 * Classe contrôleur pour gérer les interactions entre la vue et le modèle.
 */
public class ControleurBien implements ActionListener {

    // Attributs : Objets graphiques nécessaires
    private JTextField surfaceField;
    private JTextField nbrPiecesField;
    private JTextField adresseField;
    private JTextField nbrEtagesField;
    private JTextField prixField;
    private JComboBox<String> typeBienField;
    private JComboBox<String> etatBienField;
    private JTable tableBiens;
    private JTextField champRecherche; // Champ dédié pour la recherche
    private JButton ajouterButton;
    private JButton supprimerButton;
    private JButton rechercherButton;
    private JButton reinitialiserButton;
    private JButton homeButton; // Bouton "Accueil"

    private Bien modele; // Attribut pour le modèle

    private final String fichierBiens = System.getProperty("user.home") + "/biens.dat"; // Path to the file

    public ControleurBien(JTextField surfaceField, JTextField nbrPiecesField, JTextField adresseField,
                          JTextField nbrEtagesField, JTextField prixField, JComboBox<String> typeBienField,
                          JComboBox<String> etatBienField, JTable tableBiens, JTextField champRecherche,
                          JButton ajouterButton, JButton supprimerButton, JButton rechercherButton, JButton reinitialiserButton,
                          JButton homeButton, Bien modele) {
        this.surfaceField = surfaceField;
        this.nbrPiecesField = nbrPiecesField;
        this.adresseField = adresseField;
        this.nbrEtagesField = nbrEtagesField;
        this.prixField = prixField;
        this.typeBienField = typeBienField;
        this.etatBienField = etatBienField;
        this.tableBiens = tableBiens;
        this.champRecherche = champRecherche;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.rechercherButton = rechercherButton;
        this.reinitialiserButton = reinitialiserButton;
        this.homeButton = homeButton; // Stocker le bouton "Accueil"
        this.modele = modele; // Stocker l'instance de Bien
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == ajouterButton) {
            ajouterBien();
        } else if (source == supprimerButton) {
            supprimerBien();
        } else if (source == rechercherButton) {
            rechercherBien();
        } else if (source == reinitialiserButton) {
            reinitialiserTableau();
        } else if (source == homeButton) {
            // Gérer l'action du bouton "Accueil"
            InterfaceRecherche interfaceRecherche = new InterfaceRecherche();
            interfaceRecherche.setVisible(true);
            ((JFrame) SwingUtilities.getWindowAncestor(homeButton)).dispose(); // Fermer la fenêtre actuelle
        }
    }

    // Méthode pour ajouter un bien
    private void ajouterBien() {
        try {
            // Récupérer les données des champs
            double surface = Double.parseDouble(surfaceField.getText());
            int nbrPieces = Integer.parseInt(nbrPiecesField.getText());
            int nbrEtages = Integer.parseInt(nbrEtagesField.getText());
            double prix = Double.parseDouble(prixField.getText());
            String typeBien = (String) typeBienField.getSelectedItem();
            String etatBien = (String) etatBienField.getSelectedItem();
            String adresse = adresseField.getText();

            // Vérifier que tous les champs sont remplis
            if (adresse.isEmpty() || typeBien == null || etatBien == null) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
                return;
            }

            // Créer un nouvel objet Bien
            Bien bien = new Bien(null, adresse, prix, typeBien, nbrPieces, nbrEtages, surface, etatBien);

            // Ajouter le bien au modèle
            modele.ajouterBien(bien);

            // Rafraîchir le tableau
            refreshTable();
            JOptionPane.showMessageDialog(null, "Bien ajouté avec succès !");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer des valeurs valides pour les champs numériques !");
        }
    }

    // Méthode pour supprimer un bien
    private void supprimerBien() {
        int selectedRow = tableBiens.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un bien à supprimer !");
            return;
        }

        // Récupérer l'ID du bien sélectionné
        String idBien = (String) tableBiens.getValueAt(selectedRow, 0);

        // Supprimer le bien du modèle
        boolean success = modele.supprimerBien(idBien);
        if (success) {
            refreshTable();
            JOptionPane.showMessageDialog(null, "Bien supprimé avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Erreur : Bien non trouvé !");
        }
    }

    // Méthode pour rechercher un bien
    private void rechercherBien() {
        String recherche = champRecherche.getText().trim(); // Supprimer les espaces
        if (recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez entrer un ID pour rechercher !");
            return;
        }

        // Normaliser l'ID entré
        recherche = recherche.toUpperCase();

        DefaultTableModel tableModel = (DefaultTableModel) tableBiens.getModel();
        tableModel.setRowCount(0); // Effacer les anciennes données

        // Rechercher le bien correspondant à l'ID
        Bien bien = modele.rechercherBien(recherche); // Utiliser le modèle pour rechercher le bien
        if (bien != null) {
            tableModel.addRow(new Object[]{
                bien.getIdBien(),
                bien.getTypeBien(),
                bien.getEtatBien(),
                bien.getPrix(),
                bien.getSurface(),
                bien.getNbrPieces(),
                bien.getNbrEtages(),
                bien.getAdresse()
            });
        } else {
            JOptionPane.showMessageDialog(null, "Aucun bien trouvé avec l'ID : " + recherche);
        }
    }

    // Méthode pour réinitialiser le tableau
    private void reinitialiserTableau() {
        champRecherche.setText(""); // Effacer le champ de recherche
        refreshTable(); // Recharger tous les biens
    }

    // Méthode pour rafraîchir le tableau
    public void refreshTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tableBiens.getModel();
        tableModel.setRowCount(0); // Effacer les anciennes données

        // Récupérer tous les biens depuis le modèle
        List<Bien> biens = modele.getTousLesBiens(); // Utiliser le modèle pour obtenir les biens
        for (Bien bien : biens) {
            tableModel.addRow(new Object[]{
                bien.getIdBien(),
                bien.getTypeBien(),
                bien.getEtatBien(),
                bien.getPrix(),
                bien.getSurface(),
                bien.getNbrPieces(),
                bien.getNbrEtages(),
                bien.getAdresse()
            });
        }
    }

    private List<Bien> chargerBiens() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichierBiens))) {
            List<Bien> biens = (List<Bien>) ois.readObject();
            System.out.println("Biens chargés depuis le fichier : " + biens.size());
            biens.forEach(b -> System.out.println("Chargé : " + b.getIdBien()));
            return biens;
        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé, démarrage avec une liste vide.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des biens : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}