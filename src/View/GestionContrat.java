package View;

import Model.Contrat;
import Controller.ControleurContrat;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestionContrat extends JFrame {

    // Déclaration et initialisation des composants
    private JTextField dateContratField = new JTextField(15);
    private JTextField montantField = new JTextField(15);
    private JComboBox<String> typeContratField = new JComboBox<>(new String[]{"Vente", "Location"});
    private JTextField idClientField = new JTextField(15);
    private JTextField idBienField = new JTextField(15);
    private JTextField idAgentField = new JTextField(15);
    private JButton ajouterButton = new JButton("➕ Ajouter Contrat");
    private JButton rechercherButton = new JButton("🔍 Rechercher");
    private JButton reinitialiserButton = new JButton("🔄 Réinitialiser");
    private JTextField champRecherche = new JTextField(20); // Champ dédié pour la recherche
    private JButton homeButton; // Bouton "Accueil"

    private JPanel cartesPanel = new JPanel(); // Panneau pour afficher les cartes
    private JPanel mainPanel; // Panneau principal
    private Contrat modele; // Attribut pour le modèle
    private ControleurContrat co; // Déclarer le contrôleur comme un champ de classe

    public GestionContrat(Contrat modele) {
        this.modele = modele; // Initialiser le modèle
        setTitle("Gestion des Contrats");
        setSize(1200, 800); // Dimensions adaptées
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialiser l'interface utilisateur
        initialiserUI();

        // Instancier le contrôleur
        co = new ControleurContrat(
            dateContratField,
            montantField,
            typeContratField,
            idClientField,
            idBienField,
            idAgentField,
            champRecherche,
            ajouterButton,
            rechercherButton,
            reinitialiserButton,
            homeButton,
            modele,
            this // Passer la vue au contrôleur
        );

        // Relier les boutons au contrôleur
        ajouterButton.addActionListener(co);
        rechercherButton.addActionListener(co);
        reinitialiserButton.addActionListener(co);
        homeButton.addActionListener(co); // Relier le bouton "Accueil" au contrôleur

        // Afficher les cartes au démarrage
        afficherCartes();
    }

    private void initialiserUI() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0xF6F1E7)); // Beige clair
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Disposition verticale
        add(mainPanel); // Ajout du panneau principal à la fenêtre

        // Ajouter les différentes sections
        initialiserHeader(); // Ajouter le header
        ajouterEspaceEntreSections(20); // Ajouter un espace après le header
        initialiserBarreRecherche(); // Ajouter la barre de recherche
        ajouterEspaceEntreSections(20); // Ajouter un espace après la barre de recherche
        initialiserFormulaire(); // Ajouter le formulaire
        ajouterEspaceEntreSections(20); // Ajouter un espace après le formulaire
        initialiserCartesPanel(); // Ajouter le panneau des cartes
        ajouterEspaceEntreSections(20); // Ajouter un espace avant le footer
        initialiserFooter(); // Ajouter le footer
        ajouterDroitsReserves(); // Ajouter la ligne des droits réservés
    }

    private void initialiserCartesPanel() {
        cartesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20)); // Alignement à gauche, espacement horizontal et vertical de 20px
        cartesPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

        // Définir une taille minimale pour le panneau des cartes
        cartesPanel.setPreferredSize(new Dimension(0, 3000)); // Largeur flexible, hauteur fixe
        cartesPanel.setMinimumSize(new Dimension(0, 3000)); // Largeur flexible, hauteur fixe

        JScrollPane scrollPane = new JScrollPane(cartesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4E342E), 2),
            "Liste des contrats",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 16),
            new Color(0x4E342E)
        ));

        mainPanel.add(scrollPane);
    }

    public void afficherCartes(List<Contrat> contrats) {
        cartesPanel.removeAll(); // Effacer les anciennes cartes

        if (contrats.isEmpty()) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BorderLayout());
            emptyPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

            JLabel message = new JLabel("Aucun contrat disponible.");
            message.setFont(new Font("Roboto", Font.BOLD, 16));
            message.setForeground(new Color(0x4E342E));
            message.setHorizontalAlignment(SwingConstants.CENTER);

            emptyPanel.add(message, BorderLayout.CENTER);
            cartesPanel.add(emptyPanel); // Ajouter le panneau de remplissage
        } else {
            for (Contrat contrat : contrats) {
                JPanel carte = creerCarteContrat(contrat, co); // Créer une carte pour chaque contrat
                cartesPanel.add(carte);
            }
        }

        cartesPanel.revalidate();
        cartesPanel.repaint();
    }

    public void afficherCartes() {
        afficherCartes(modele.getTousLesContrats()); // Appeler la surcharge avec tous les contrats
    }

    private JPanel creerCarteContrat(Contrat contrat, ControleurContrat co) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(300, 200)); // Dimensions adaptées
        carte.setBorder(new LineBorder(new Color(0x4E342E), 2));
        carte.setBackground(Color.WHITE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = contrat.getDateContrat() != null ? dateFormat.format(contrat.getDateContrat()) : "Non spécifiée";

        JLabel idLabel = new JLabel("ID Contrat : " + contrat.getIdContrat());
        idLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        idLabel.setForeground(new Color(0x4E342E));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titre = new JLabel(contrat.getTypeContrat() + " - " + contrat.getMontant() + " €");
        titre.setFont(new Font("Roboto", Font.BOLD, 16));
        titre.setForeground(new Color(0x4E342E));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel date = new JLabel("Date : " + formattedDate);
        date.setFont(new Font("Roboto", Font.PLAIN, 14));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel agentLabel = new JLabel("ID Agent : " + (contrat.getAgent() != null ? contrat.getAgent().getIdPersonne() : "Non spécifié"));
        agentLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        agentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel clientLabel = new JLabel("ID Client : " + (contrat.getClient() != null ? contrat.getClient().getIdPersonne() : "Non spécifié"));
        clientLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bienLabel = new JLabel("ID Bien : " + (contrat.getBien() != null ? contrat.getBien().getIdBien() : "Non spécifié"));
        bienLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        bienLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton supprimerButton = new JButton("🗑 Supprimer");
        supprimerButton.setBackground(new Color(0x795548));
        supprimerButton.setForeground(Color.WHITE);
        supprimerButton.setFocusPainted(false);
        supprimerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        supprimerButton.setActionCommand(contrat.getIdContrat());
        supprimerButton.addActionListener(co);

        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(idLabel);
        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(titre);
        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(date);
        carte.add(agentLabel);
        carte.add(clientLabel);
        carte.add(bienLabel);
        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(supprimerButton);

        return carte;
    }

    private void ajouterEspaceEntreSections(int height) {
        mainPanel.add(Box.createRigidArea(new Dimension(0, height)));
    }

    private void initialiserHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0x4E342E)); // Marron foncé
        headerPanel.setPreferredSize(new Dimension(0, 120)); // Hauteur réduite à 120

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        JButton homeButton = new JButton("🏠 Accueil");
        styleButton(homeButton, new Color(0x795548));
        this.homeButton = homeButton;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 20, 0, 0);
        leftPanel.add(homeButton, gbc);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("GESTION DES CONTRATS");
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel);
    }

    private void initialiserBarreRecherche() {
        JPanel barreRecherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barreRecherchePanel.setBackground(new Color(0xF6F1E7));

        JLabel labelRecherche = new JLabel("Rechercher :");
        rechercherButton = new JButton("🔍 Rechercher");
        styleButton(rechercherButton, new Color(0x795548));

        reinitialiserButton = new JButton("🔄 Réinitialiser");
        styleButton(reinitialiserButton, new Color(0x795548));

        barreRecherchePanel.add(labelRecherche);
        barreRecherchePanel.add(champRecherche);
        barreRecherchePanel.add(rechercherButton);
        barreRecherchePanel.add(reinitialiserButton);

        mainPanel.add(barreRecherchePanel);
    }

    private void initialiserFormulaire() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(0xF6F1E7));

        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4E342E), 2),
            "Formulaire de gestion",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 16),
            new Color(0x4E342E)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Date (dd/MM/yyyy) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateContratField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Montant (€) :"), gbc);
        gbc.gridx = 3;
        formPanel.add(montantField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Type de contrat :"), gbc);
        gbc.gridx = 1;
        formPanel.add(typeContratField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("ID Client :"), gbc);
        gbc.gridx = 3;
        formPanel.add(idClientField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("ID Bien :"), gbc);
        gbc.gridx = 1;
        formPanel.add(idBienField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("ID Agent :"), gbc);
        gbc.gridx = 3;
        formPanel.add(idAgentField, gbc);

        mainPanel.add(formPanel);
    }

    private void initialiserFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        footerPanel.setBackground(new Color(0xF6F1E7));

        styleButton(ajouterButton, new Color(0x4E342E));
        footerPanel.add(ajouterButton);

        mainPanel.add(footerPanel);
    }

    private void ajouterDroitsReserves() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0xF6F1E7));

        JLabel footerLabel = new JLabel("© 2025 Gestion des Contrats - Tous droits réservés");
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

    public void refreshCartes() {
        List<Contrat> contrats = modele.getTousLesContrats();
        afficherCartes(contrats);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Contrat modele = new Contrat(System.getProperty("user.home") + "/contrats.dat");
            GestionContrat vue = new GestionContrat(modele);
            vue.setVisible(true);
        });
    }
}