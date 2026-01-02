package View;

import Model.Bien;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class GestionBien extends JFrame {

    // Déclaration et initialisation des composants
    private JTextField surfaceField = new JTextField(15);
    private JTextField nbrPiecesField = new JTextField(15);
    private JTextField adresseField = new JTextField(15);
    private JTextField nbrEtagesField = new JTextField(15);
    private JTextField prixField = new JTextField(15);
    private JComboBox<String> typeBienField = new JComboBox<>(new String[]{"Maison", "Appartement", "Terrain"});
    private JComboBox<String> etatBienField = new JComboBox<>(new String[]{"À vendre", "Loué"});
    private JButton ajouterButton = new JButton("➕ Ajouter Bien");
    private JButton supprimerButton = new JButton("🗑️ Supprimer Bien");
    private JButton rechercherButton = new JButton("🔍 Rechercher");
    private JButton reinitialiserButton = new JButton("🔄 Réinitialiser");
    private JTable tableBiens = new JTable();
    private JTextField champRecherche = new JTextField(20); // Champ dédié pour la recherche
    private JButton homeButton; // Bouton "Accueil"

    private JPanel mainPanel; // Panneau principal
    private Bien modele; // Attribut pour le modèle

    public GestionBien(Bien modele) {
        this.modele = modele; // Initialiser le modèle
        setTitle("Gestion des Biens Immobiliers");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialiser l'interface utilisateur
        initialiserUI();

        // Instancier le contrôleur
        Controller.ControleurBien co = new Controller.ControleurBien(
            surfaceField,
            nbrPiecesField,
            adresseField,
            nbrEtagesField,
            prixField,
            typeBienField,
            etatBienField,
            tableBiens,
            champRecherche, // Utiliser le champ dédié pour la recherche
            ajouterButton,
            supprimerButton,
            rechercherButton,
            reinitialiserButton,
            homeButton, // Passer le bouton "Accueil" au contrôleur
            modele
        );

        // Relier les boutons au contrôleur
        ajouterButton.addActionListener(co);
        supprimerButton.addActionListener(co);
        rechercherButton.addActionListener(co);
        reinitialiserButton.addActionListener(co);
        homeButton.addActionListener(co); // Relier le bouton "Accueil" au contrôleur

        // Rafraîchir le tableau au démarrage
        co.refreshTable();
    }

    public GestionBien() {
        //TODO Auto-generated constructor stub
    }

    private void initialiserUI() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0xF6F1E7)); // Beige clair
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel); // Ajout du panneau principal à la fenêtre

        // Ajouter les différentes sections
        initialiserHeader(); // Ajouter le header
        initialiserBarreRecherche(); // Ajouter la barre de recherche
        initialiserFormulaire(); // Ajouter le formulaire
        ajouterEspaceEntreSections(20); // Ajouter un espace entre les sections
        initialiserTableau(); // Ajouter le tableau
        ajouterEspaceEntreSections(20); // Ajouter un espace avant le footer
        initialiserFooter(); // Ajouter le footer
        ajouterDroitsReserves(); // Ajouter la ligne des droits réservés
    }

    private void initialiserHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x4E342E)); // Marron foncé
        headerPanel.setPreferredSize(new Dimension(0, 120)); // Hauteur réduite à 120

        // Bouton "Accueil"
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false); // Rendre le panneau transparent
        homeButton = new JButton("🏠 Accueil"); // Initialiser le bouton
        styleButton(homeButton, new Color(0x795548)); // Appliquer le style au bouton

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 0); // Espacement à gauche
        leftPanel.add(homeButton, gbc);
        headerPanel.add(leftPanel, BorderLayout.WEST); // Ajouter le panneau à gauche du header

        // Titre centré
        JLabel titleLabel = new JLabel("GESTION DE BIENS");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 36)); // Police et taille
        titleLabel.setForeground(Color.WHITE); // Texte blanc
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrer le texte horizontalement
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel); // Ajouter le header au panneau principal
    }

    private void initialiserBarreRecherche() {
        JPanel barreRecherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barreRecherchePanel.setBackground(new Color(0xF6F1E7)); // Beige clair

        JLabel labelRecherche = new JLabel("Rechercher :");
        rechercherButton = new JButton("🔍 Rechercher");
        styleButton(rechercherButton, new Color(0x795548)); // Marron foncé

        reinitialiserButton = new JButton("🔄 Réinitialiser");
        styleButton(reinitialiserButton, new Color(0x795548)); // Marron foncé

        barreRecherchePanel.add(labelRecherche);
        barreRecherchePanel.add(champRecherche); // Utiliser le champ dédié
        barreRecherchePanel.add(rechercherButton);
        barreRecherchePanel.add(reinitialiserButton);

        mainPanel.add(barreRecherchePanel);
    }

    private void initialiserFormulaire() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout()); // Utiliser GridBagLayout pour un placement flexible
        formPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4E342E), 2), // Bordure marron
            "Formulaire de gestion", // Titre
            TitledBorder.LEFT, // Alignement du titre
            TitledBorder.TOP, // Position du titre
            new Font("Roboto", Font.BOLD, 16), // Police du titre
            new Color(0x4E342E) // Couleur du titre
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Marges entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Surface (m²) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(surfaceField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Nombre de pièces :"), gbc);
        gbc.gridx = 3;
        formPanel.add(nbrPiecesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Adresse :"), gbc);
        gbc.gridx = 1;
        formPanel.add(adresseField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Nombre d'étages :"), gbc);
        gbc.gridx = 3;
        formPanel.add(nbrEtagesField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Prix (€) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(prixField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Type de bien :"), gbc);
        gbc.gridx = 3;
        formPanel.add(typeBienField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("État du bien :"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(etatBienField, gbc);

        mainPanel.add(formPanel);
    }

    private void initialiserTableau() {
        String[] columnNames = {"ID", "Type", "État", "Prix", "Surface", "Pièces", "Étages", "Adresse"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tableBiens = new JTable(tableModel);

        tableBiens.setRowHeight(30);
        tableBiens.setFont(new Font("Roboto", Font.PLAIN, 14));
        tableBiens.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        tableBiens.getTableHeader().setBackground(new Color(0x4E342E));
        tableBiens.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(tableBiens);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4E342E), 2),
            "Liste des biens",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 16),
            new Color(0x4E342E)
        ));

        mainPanel.add(tableScrollPane);
    }

    private void initialiserFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footerPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

        styleButton(ajouterButton, new Color(0x4E342E)); // Marron foncé
        footerPanel.add(ajouterButton);

        styleButton(supprimerButton, new Color(0x4E342E)); // Marron foncé
        footerPanel.add(supprimerButton);

        mainPanel.add(footerPanel);
    }

    private void ajouterEspaceEntreSections(int height) {
        mainPanel.add(Box.createRigidArea(new Dimension(0, height)));
    }

    private void ajouterDroitsReserves() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0xF6F1E7));

        JLabel footerLabel = new JLabel("© 2025 Gestion de Biens - Tous droits réservés");
        footerLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(0x4E342E));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(footerLabel, BorderLayout.CENTER);
        mainPanel.add(footerPanel);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Roboto", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Bien modele = new Bien(System.getProperty("user.home") + "/biens.dat");
            GestionBien vue = new GestionBien(modele);
            vue.setVisible(true);
        });
    }
}