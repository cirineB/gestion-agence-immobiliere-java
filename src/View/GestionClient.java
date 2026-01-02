package View;

import Model.Client;
import Controller.ControleurClient;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GestionClient extends JFrame {

    // Déclaration et initialisation des composants
    private JTextField nomField = new JTextField(15);
    private JTextField prenomField = new JTextField(15);
    private JTextField telField = new JTextField(15);
    private JTextField emailField = new JTextField(15);
    private JComboBox<String> etatCivilCombo = new JComboBox<>(new String[]{"Marié(e)", "Célibataire"});
    private JComboBox<String> typeDemandeCombo = new JComboBox<>(new String[]{"Achat", "Location"});
    private JComboBox<String> situationProCombo = new JComboBox<>(new String[]{"Salarié(e)", "Indépendant(e)", "Retraité(e)"});
    private JButton ajouterButton = new JButton("✅ Ajouter Client");
    private JButton supprimerButton = new JButton("🗑️ Supprimer Client");
    private JButton rechercherButton = new JButton("🔍 Rechercher");
    private JButton reinitialiserButton = new JButton("🔄 Réinitialiser");
    private JTable tableClients = new JTable();
    private JTextField champRecherche = new JTextField(20); // Champ dédié pour la recherche
    private JButton homeButton; // Bouton "Accueil"

    private JPanel mainPanel; // Panneau principal
    private Client modele; // Attribut pour le modèle

    public GestionClient(Client modele) {
        this.modele = modele; // Initialiser le modèle
        setTitle("Gestion des Clients");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialiser l'interface utilisateur
        initialiserUI();

        // Instancier le contrôleur
        ControleurClient controleur = new ControleurClient(
            nomField, prenomField, telField, emailField,
            etatCivilCombo, typeDemandeCombo, situationProCombo,
            tableClients, champRecherche, ajouterButton, supprimerButton, rechercherButton,reinitialiserButton, homeButton, modele
        );

        // Relier les boutons au contrôleur
        ajouterButton.addActionListener(controleur);
        supprimerButton.addActionListener(controleur);
        rechercherButton.addActionListener(controleur);
        reinitialiserButton.addActionListener(controleur); // Relier le bouton "Réinitialiser" au contrôleur
        homeButton.addActionListener(controleur); // Relier le bouton "Accueil" au contrôleur

        // Rafraîchir le tableau au démarrage
        controleur.refreshTable();
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
        JButton homeButton = new JButton("🏠 Accueil");
        styleButton(homeButton, new Color(0x795548)); // Appliquer le style au bouton
        this.homeButton = homeButton; // Stocker le bouton pour le relier au contrôleur

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 0); // Espacement à gauche
        leftPanel.add(homeButton, gbc);
        headerPanel.add(leftPanel, BorderLayout.WEST); // Ajouter le panneau à gauche du header

        // Titre centré
        JLabel titleLabel = new JLabel("GESTION DES CLIENTS");
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
            "Formulaire de gestion des clients", // Titre
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
        formPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 3;
        formPanel.add(prenomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        formPanel.add(telField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 3;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("État Civil :"), gbc);
        gbc.gridx = 1;
        formPanel.add(etatCivilCombo, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Type de Demande :"), gbc);
        gbc.gridx = 3;
        formPanel.add(typeDemandeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Situation Pro. :"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(situationProCombo, gbc);

        mainPanel.add(formPanel);
    }

    private void initialiserTableau() {
        String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email", "État Civil", "Type de Demande", "Situation Pro."};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tableClients = new JTable(tableModel);

        tableClients.setRowHeight(30);
        tableClients.setFont(new Font("Roboto", Font.PLAIN, 14));
        tableClients.getTableHeader().setFont(new Font("Roboto", Font.BOLD, 14));
        tableClients.getTableHeader().setBackground(new Color(0x4E342E));
        tableClients.getTableHeader().setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(tableClients);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4E342E), 2),
            "Liste des Clients",
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

        JLabel footerLabel = new JLabel("© 2025 Gestion des Clients - Tous droits réservés");
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
            Client modele = new Client(System.getProperty("user.home") + "/clients.dat");
            GestionClient vue = new GestionClient(modele);
            vue.setVisible(true);
        });
    }
}