package View;

import Model.Visite;
import Controller.ControleurVisite;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class GestionVisite extends JFrame {

    // Déclaration et initialisation des composants
    private JTextField dateVisiteField = new JTextField(15);
    private JTextField heureVisiteField = new JTextField(15);
    private JTextField commentaireField = new JTextField(15);
    private JTextField idClientField = new JTextField(15);
    private JTextField idBienField = new JTextField(15);
    private JTextField idAgentField = new JTextField(15);
    private JButton ajouterButton = new JButton("➕ Ajouter Visite");
    private JButton rechercherButton = new JButton("🔍 Rechercher");
    private JButton reinitialiserButton = new JButton("🔄 Réinitialiser");
    private JTextField champRecherche = new JTextField(20); // Champ dédié pour la recherche
    private JButton homeButton; // Bouton "Accueil"

    private JPanel cartesPanel = new JPanel(); // Panneau pour afficher les cartes
    private JPanel mainPanel; // Panneau principal
    private Visite modele; // Attribut pour le modèle
    private ControleurVisite co; // Déclarer le contrôleur comme un champ de classe

    public GestionVisite(Visite modele) {
        this.modele = modele; // Initialiser le modèle
        setTitle("Gestion des Visites");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Afficher en plein écran
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialiser l'interface utilisateur
        initialiserUI();

        // Instancier le contrôleur
        co = new ControleurVisite(
            dateVisiteField,
            heureVisiteField,
            commentaireField,
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
            "Liste des visites",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Roboto", Font.BOLD, 16),
            new Color(0x4E342E)
        ));

        mainPanel.add(scrollPane);
    }

    public void afficherCartes(List<Visite> visites) {
        cartesPanel.removeAll(); // Effacer les anciennes cartes

        if (visites.isEmpty()) {
            // Ajouter un panneau de remplissage pour conserver la taille
            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BorderLayout());
            emptyPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

            JLabel message = new JLabel("Aucune visite disponible.");
            message.setFont(new Font("Roboto", Font.BOLD, 16));
            message.setForeground(new Color(0x4E342E));
            message.setHorizontalAlignment(SwingConstants.CENTER);

            emptyPanel.add(message, BorderLayout.CENTER);
            cartesPanel.add(emptyPanel); // Ajouter le panneau de remplissage
        } else {
            for (Visite visite : visites) {
                JPanel carte = creerCarteVisite(visite, co); // Créer une carte pour chaque visite
                cartesPanel.add(carte);
            }
        }

        cartesPanel.revalidate();
        cartesPanel.repaint();
    }

    // Méthode existante pour afficher toutes les visites
    public void afficherCartes() {
        afficherCartes(modele.getToutesLesVisites()); // Appeler la surcharge avec toutes les visites
    }

    private JPanel creerCarteVisite(Visite visite, ControleurVisite co) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(300, 200));
        carte.setBorder(new LineBorder(new Color(0x4E342E), 2));
        carte.setBackground(Color.WHITE);

        // Formater la date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = visite.getDateVisite() != null ? dateFormat.format(visite.getDateVisite()) : "Non spécifiée";

        // Ajouter les informations à la carte
        JLabel idLabel = new JLabel("ID Visite : " + visite.getIdVisite());
        idLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        idLabel.setForeground(new Color(0x4E342E));
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel date = new JLabel("Date : " + formattedDate);
        date.setFont(new Font("Roboto", Font.PLAIN, 14));
        date.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel heure = new JLabel("Heure : " + visite.getHeureVisite());
        heure.setFont(new Font("Roboto", Font.PLAIN, 14));
        heure.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel commentaire = new JLabel("Commentaire : " + visite.getCommentaire());
        commentaire.setFont(new Font("Roboto", Font.PLAIN, 14));
        commentaire.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel clientLabel = new JLabel("ID Client : " + (visite.getClient() != null ? visite.getClient().getIdPersonne() : "Non spécifié"));
        clientLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        clientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel bienLabel = new JLabel("ID Bien : " + (visite.getBien() != null ? visite.getBien().getIdBien() : "Non spécifié"));
        bienLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        bienLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel agentLabel = new JLabel("ID Agent : " + (visite.getAgent() != null ? visite.getAgent().getIdPersonne() : "Non spécifié"));
        agentLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        agentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton supprimerButton = new JButton("🗑 Supprimer");
        supprimerButton.setBackground(new Color(0x795548));
        supprimerButton.setForeground(Color.WHITE);
        supprimerButton.setFocusPainted(false);
        supprimerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        supprimerButton.setActionCommand(String.valueOf(visite.getIdVisite()));
        supprimerButton.addActionListener(co);

        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(idLabel);
        carte.add(Box.createRigidArea(new Dimension(0, 10)));
        carte.add(date);
        carte.add(heure);
        carte.add(commentaire);
        carte.add(clientLabel);
        carte.add(bienLabel);
        carte.add(agentLabel);
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
        JLabel titleLabel = new JLabel("GESTION DES VISITES");
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
        formPanel.add(new JLabel("Date (dd/MM/yyyy) :"), gbc);
        gbc.gridx = 1;
        formPanel.add(dateVisiteField, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Heure :"), gbc);
        gbc.gridx = 3;
        formPanel.add(heureVisiteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Commentaire :"), gbc);
        gbc.gridx = 1;
        formPanel.add(commentaireField, gbc);

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
        footerPanel.setBackground(new Color(0xF6F1E7)); // Beige clair

        styleButton(ajouterButton, new Color(0x4E342E)); // Marron foncé
        footerPanel.add(ajouterButton);

        mainPanel.add(footerPanel);
    }

    private void ajouterDroitsReserves() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(0xF6F1E7));

        JLabel footerLabel = new JLabel("© 2025 Gestion des Visites - Tous droits réservés");
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

    // Méthode pour rafraîchir les cartes
    public void refreshCartes() {
        // Récupérer toutes les visites depuis le modèle
        List<Visite> visites = modele.getToutesLesVisites(); // Utiliser le modèle pour obtenir les visites

        // Notifier la vue pour afficher les cartes mises à jour
        afficherCartes(visites); // Appeler la méthode de la vue pour afficher les cartes
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Visite modele = new Visite(System.getProperty("user.home") + "/visites.dat");
            GestionVisite vue = new GestionVisite(modele);
            vue.setVisible(true);
        });
    }
}
